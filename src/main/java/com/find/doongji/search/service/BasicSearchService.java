package com.find.doongji.search.service;

import com.find.doongji.address.repository.AddressRepository;
import com.find.doongji.address.util.AddressUtil;
import com.find.doongji.apt.client.AptDetailClient;
import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.apt.payload.response.DanjiCode;
import com.find.doongji.apt.service.AptService;
import com.find.doongji.auth.service.AuthService;
import com.find.doongji.location.payload.response.DongCode;
import com.find.doongji.location.repository.LocationRepository;
import com.find.doongji.search.enums.SimilarityScore;
import com.find.doongji.search.payload.response.SearchResponse;
import com.find.doongji.search.payload.response.SearchResult;
import com.find.doongji.apt.repository.AptRepository;
import com.find.doongji.history.payload.request.HistoryRequest;
import com.find.doongji.history.service.HistoryService;
import com.find.doongji.search.client.RecommendClient;
import com.find.doongji.search.payload.request.SearchRequest;
import com.find.doongji.search.payload.response.RecommendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class BasicSearchService implements SearchService {

    private final RecommendClient recClient;
    private final AptDetailClient aptClient;

    private final AddressRepository addressRepository;
    private final AptRepository aptRepository;
    private final LocationRepository locationRepository;

    private final HistoryService historyService;
    private final AptService aptService;
    private final AuthService authService;

    private static final int TOP_K = 50;

    @Override
    @Transactional
    public List<SearchResponse> search(SearchRequest searchRequest) throws Exception {
        // Handle empty query case
        List<SearchResponse> responses;

        if (searchRequest.getQuery() == null || searchRequest.getQuery().trim().isEmpty()) {
            List<AptInfo> aptInfos = filterAptInfosByOverlap(aptRepository.findAllAptInfos(), searchRequest);
            responses = aptInfos.stream()
                    .map(aptInfo -> new SearchResponse(SimilarityScore.NONE, aptInfo))
                    .toList();
        } else {
            // Query is not empty, use recommendation client
            List<RecommendResponse> recommendResponses = recClient.getRecommendation(searchRequest.getQuery(), TOP_K).stream()
                    .filter(distinctByKey(RecommendResponse::getDanjiId))
                    .toList();
            responses = new ArrayList<>();

            outerLoop:
            for (RecommendResponse recommendResponse : recommendResponses) {
                String bjdCode = addressRepository.selectBjdCodeByDanjiId(recommendResponse.getDanjiId());
                if (bjdCode == null) {
                    continue;
                }

                if (searchRequest.getLocationFilter() != null) {
                    List<DongCode> dongCodes = locationRepository.selectDongCodeByStartsWith(AddressUtil.cleanAddress(searchRequest.getLocationFilter()));
                    if (dongCodes.isEmpty()) {
                        continue;
                    }
                }

                List<DanjiCode> danjiCodes = aptClient.getDanjiCodeList(bjdCode);
                System.out.println("BjdCode: "+ bjdCode);
                for (DanjiCode danjiCode : danjiCodes) {
                    System.out.println("DanjiCode: " + danjiCode);

                }
                List<AptInfo> unverifiedAptInfos = aptService.findAptInfoByDongCode(bjdCode);

                for (DanjiCode danjiCode : danjiCodes) {
                    System.out.println("DanjiCode: " + danjiCode);
                    for (AptInfo aptInfo : unverifiedAptInfos) {
                        if (!compareAptNames(aptInfo.getAptNm(), danjiCode.getKaptName())) {
                            continue;
                        }
                        if (!matchesSearchCriteria(aptInfo, searchRequest)) continue;
                        responses.add(new SearchResponse(SimilarityScore.classify(recommendResponse.getSimilarity()), aptInfo));
                        System.out.println("Compare: aptInfo("+aptInfo.getAptNm()+"), danjiCode("+danjiCode.getKaptName()+")");

                        if (responses.size() >= TOP_K) {
                            break outerLoop;
                        }
                        break;
                    }
                }



            }


        }

        if (authService.isAuthenticated()) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            historyService.addHistory(new HistoryRequest(username, searchRequest.getQuery()));
        }

        return responses;
    }

    public boolean compareAptNames(String aptInfoName, String danjiName) {
        if (aptInfoName == null || danjiName == null) {
            return false;
        }

        String normalizedAptInfoName = cleanDanjiName(aptInfoName);
        String normalizedDanjiName = cleanDanjiName(danjiName);

        return normalizedDanjiName.startsWith(normalizedAptInfoName);
    }


    @Override
    public SearchResult viewSearched(String aptSeq) throws Exception {
        AptInfo aptInfo = aptRepository.selectAptInfoByAptSeq(aptSeq);
        List<DanjiCode> danjiCodes = aptClient.getDanjiCodeList(aptInfo.getSggCd() + aptInfo.getUmdCd());

        String aptName = aptInfo.getAptNm();

        for (DanjiCode danjiCode : danjiCodes) {
            String cleanedDanjiName = cleanDanjiName(danjiCode.getKaptName());
            if (!cleanedDanjiName.equals(aptName)) continue;

            return aptClient.getAptDetail(danjiCode.getKaptCode());
        }
        throw new Exception("viewSearched: No matching danji code found for apt seq: " + aptSeq);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


    private String cleanDanjiName(String danjiName) {
        if (danjiName == null || danjiName.isEmpty()) {
            return "";
        }
        return danjiName
                .replaceAll("[^가-힣0-9a-zA-Z]", "")
                .replace("더블유", "W")
                .replace("아파트", "")
                .replaceAll("\\s+", "")
                .trim();
    }


    private List<AptInfo> filterAptInfosByOverlap(List<AptInfo> aptInfos, SearchRequest searchRequest) {
        return aptInfos.stream()
                .filter(aptInfo -> matchesSearchCriteria(aptInfo, searchRequest))
                .toList();
    }

    private boolean matchesSearchCriteria(AptInfo aptInfo, SearchRequest searchRequest) {

        // Check price range overlap
        if (!checkPriceOverlap(aptInfo, searchRequest)) {
            System.out.println("Price range does not overlap");
            return false;
        }

        // Check area range overlap
        if (!checkAreaOverlap(aptInfo, searchRequest)) {
            System.out.println("Area range does not overlap");
            return false;
        }

        // Check location filter
        if (!checkLocationFilter(aptInfo, searchRequest)) {
            System.out.println("Location filter does not match");
            return false;
        }


        return true;
    }

    private boolean checkPriceOverlap(AptInfo aptInfo, SearchRequest searchRequest) {
        if (searchRequest.getMinPrice() == null && searchRequest.getMaxPrice() == null) {
            return true; // No filter applied
        }

        String minDealAmount = aptInfo.getMinDealAmount();
        String maxDealAmount = aptInfo.getMaxDealAmount();

        if (minDealAmount == null || maxDealAmount == null) return false;
        return isOverlappingRange(
                    parseToDouble(minDealAmount),
                    parseToDouble(maxDealAmount),
                    searchRequest.getMinPrice(),
                    searchRequest.getMaxPrice()
            );
    }

    private boolean checkAreaOverlap(AptInfo aptInfo, SearchRequest searchRequest) {
        if (searchRequest.getMinArea() == null && searchRequest.getMaxArea() == null) {
            return true;
        }

        String minExcluUseAr = aptInfo.getMinExcluUseAr();
        String maxExcluUseAr = aptInfo.getMaxExcluUseAr();

        if (minExcluUseAr == null || maxExcluUseAr == null) return false;
        return isOverlappingRange(
                    parseToDouble(minExcluUseAr),
                    parseToDouble(maxExcluUseAr),
                    searchRequest.getMinArea(),
                    searchRequest.getMaxArea()
            );
    }

    private boolean checkLocationFilter(AptInfo aptInfo, SearchRequest searchRequest) {
        if (searchRequest.getLocationFilter() == null) {
            return true;
        }

        DongCode dongCode = locationRepository.selectDongCodeByDongcode(aptInfo.getSggCd() + aptInfo.getUmdCd());
        if (dongCode != null) {
            String startAddress = dongCode.getSidoName() + " " + dongCode.getGugunName() + " " + dongCode.getDongName();
            String cleaned = AddressUtil.cleanAddress(searchRequest.getLocationFilter());
            return startAddress.startsWith(cleaned);
        }

        return false;
    }


    private boolean isOverlappingRange(Number minApt, Number maxApt, Number minRequest, Number maxRequest) {
        if (minApt == null || maxApt == null) return true; // Consider it overlapping if apt range is undefined.

        // Default request range if null
        minRequest = (minRequest != null) ? minRequest : Double.MIN_VALUE;
        maxRequest = (maxRequest != null) ? maxRequest : Double.MAX_VALUE;

        // Overlap exists if the ranges intersect
        return Math.max(minApt.doubleValue(), minRequest.doubleValue()) <= Math.min(maxApt.doubleValue(), maxRequest.doubleValue());
    }



    private Double parseToDouble(String value) {
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Double.MIN_VALUE;
        }
    }
}
