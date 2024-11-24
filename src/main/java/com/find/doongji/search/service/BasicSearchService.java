package com.find.doongji.search.service;

import com.find.doongji.address.payload.response.AddressMappingResponse;
import com.find.doongji.address.repository.AddressRepository;
import com.find.doongji.address.util.AddressUtil;
import com.find.doongji.apt.client.AptDetailClient;
import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.apt.repository.AptRepository;
import com.find.doongji.apt.service.AptService;
import com.find.doongji.auth.service.AuthService;
import com.find.doongji.danji.payload.response.DanjiCode;
import com.find.doongji.danji.repository.DanjiRepository;
import com.find.doongji.history.payload.request.HistoryRequest;
import com.find.doongji.history.service.HistoryService;
import com.find.doongji.like.service.LikeService;
import com.find.doongji.location.payload.response.DongCode;
import com.find.doongji.location.repository.LocationRepository;
import com.find.doongji.search.client.RecommendClient;
import com.find.doongji.search.enums.SimilarityScore;
import com.find.doongji.search.payload.request.SearchRequest;
import com.find.doongji.search.payload.response.RecommendResponse;
import com.find.doongji.search.payload.response.SearchDetailResponse;
import com.find.doongji.search.payload.response.SearchResponse;
import com.find.doongji.search.payload.response.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicSearchService implements SearchService {

    private static final int TOP_K = 10000;

    private final RecommendClient recClient;
    private final AptDetailClient aptClient;

    private final AddressRepository addressRepository;
    private final AptRepository aptRepository;
    private final LocationRepository locationRepository;
    private final DanjiRepository danjiRepository;

    private final HistoryService historyService;
    private final AuthService authService;
    private final LikeService likeService;

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Override
    @Transactional
    public List<SearchResponse> search(SearchRequest searchRequest) throws Exception {
        // Handle empty query case
        List<SearchResponse> searchResponses;

        if (searchRequest.getQuery() == null || searchRequest.getQuery().trim().isEmpty()) {
            List<AptInfo> aptInfos = filterAptInfosByOverlap(aptRepository.findAllAptInfos(), searchRequest);
            searchResponses = aptInfos.stream()
                    .map(aptInfo -> new SearchResponse(SimilarityScore.NONE, aptInfo, likeService.viewLike(aptInfo.getAptSeq())))
                    .toList();
        } else {
            // Query is not empty, use recommendation client
            List<RecommendResponse> recommendResponses = recClient.getRecommendation(searchRequest.getQuery(), TOP_K).stream()
                    .filter(distinctByKey(RecommendResponse::getDanjiId))
                    .toList();

            searchResponses = new ArrayList<>();


            Map<Long, RecommendResponse> recommendResponseMap = recommendResponses.stream()
                    .collect(Collectors.toMap(RecommendResponse::getDanjiId, Function.identity()));

            List<Long> danjiIds = new ArrayList<>(recommendResponseMap.keySet());

            List<AddressMappingResponse> mappings = addressRepository.selectAddressMappingByDanjiIdList(danjiIds);

            Map<String, List<Long>> aptSeqToDanjiIdsMap = mappings.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(
                            AddressMappingResponse::getAptSeq,
                            mapping -> new ArrayList<>(List.of(mapping.getDanjiId())),
                            (existing, replacement) -> {
                                existing.addAll(replacement);
                                return existing;
                            }
                    ));

            List<String> aptSeqList = new ArrayList<>(aptSeqToDanjiIdsMap.keySet());
            List<AptInfo> aptInfos = aptRepository.selectAptInfoByAptSeqList(aptSeqList);

            searchResponses = filterAptInfosByOverlap(aptInfos, searchRequest)
                    .stream()
                    .flatMap(aptInfo -> {
                        List<Long> danjiIdList = aptSeqToDanjiIdsMap.getOrDefault(aptInfo.getAptSeq(), List.of());

                        return danjiIdList.stream()
                                .map(danjiId -> recommendResponseMap.get(danjiId))
                                .filter(Objects::nonNull)
                                .map(response -> Map.entry(response, aptInfo)); // aptInfo와 함께 매핑
                    })
                    .sorted(Comparator.comparing((Map.Entry<RecommendResponse, AptInfo> entry) -> entry.getKey().getSimilarity()).reversed())
                    .map(entry -> new SearchResponse(
                            SimilarityScore.classify(entry.getKey().getSimilarity()),
                            entry.getValue(),
                            likeService.viewLike(entry.getValue().getAptSeq()))
                    )
                    .toList();


        }

        trackSearchHistory(searchRequest);

        return searchResponses;
    }

    @Override
    public SearchDetailResponse viewSearched(String aptSeq) throws Exception {

        AptInfo aptInfo = aptRepository.selectAptInfoByAptSeq(aptSeq);
        if (aptInfo == null) {
            throw new Exception("viewSearched: No matching apt seq found: " + aptSeq);
        }
        List<DanjiCode> danjiCodes = danjiRepository.selectByAptNm(aptInfo.getAptNm());
        SearchResult searchResult= null;
        for (DanjiCode danjiCode : danjiCodes) {
            if (danjiCode.getAs3().trim().equals(aptInfo.getUmdNm().trim())) {
                searchResult = aptClient.getAptDetail(danjiCode.getKaptCode());
                if (!searchResult.getKaptName().isEmpty()) {
                    break;
                }
            }
        }

        return SearchDetailResponse.builder()
                .searchResult(searchResult)
                .isLiked(likeService.viewLike(aptSeq))
                .build();
    }


    private void trackSearchHistory(SearchRequest searchRequest) {
        if (authService.isAuthenticated() && searchRequest.getQuery() != null) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            historyService.addHistory(new HistoryRequest(username, searchRequest.getQuery()));
        }
    }


    private List<AptInfo> filterAptInfosByOverlap(List<AptInfo> aptInfos, SearchRequest searchRequest) {
        return aptInfos.stream()
                .filter(aptInfo -> matchesSearchCriteria(aptInfo, searchRequest))
                .toList();
    }

    private boolean matchesSearchCriteria(AptInfo aptInfo, SearchRequest searchRequest) {

        // Check price range overlap
        if (!checkPriceOverlap(aptInfo, searchRequest)) {
            return false;
        }

        // Check area range overlap
        if (!checkAreaOverlap(aptInfo, searchRequest)) {
            return false;
        }

        // Check location filter
        if (!checkLocationFilter(aptInfo, searchRequest)) {
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
            String startAddress = dongCode.getSidoName();
            if (!startAddress.equals(dongCode.getGugunName())) {
                startAddress+= " " + dongCode.getGugunName();
            }
            startAddress += " " + dongCode.getDongName();
            startAddress = AddressUtil.cleanAddress(startAddress);

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
