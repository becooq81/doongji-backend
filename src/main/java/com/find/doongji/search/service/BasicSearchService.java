package com.find.doongji.search.service;

import com.find.doongji.address.repository.AddressRepository;
import com.find.doongji.address.util.RoadAddressUtil;
import com.find.doongji.apt.client.AptDetailClient;
import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.apt.payload.response.DanjiCode;
import com.find.doongji.apt.service.AptService;
import com.find.doongji.auth.service.AuthService;
import com.find.doongji.location.payload.response.DongCode;
import com.find.doongji.location.repository.LocationRepository;
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

    private static final int TOP_K = 1000;

    @Override
    @Transactional
    public List<AptInfo> search(SearchRequest searchRequest) throws Exception {
        // Handle empty query case
        List<AptInfo> aptInfos;

        if (searchRequest.getQuery() == null || searchRequest.getQuery().trim().isEmpty()) {
            aptInfos = aptRepository.findAllAptInfos();
        } else {
            // Query is not empty, use recommendation client
            List<RecommendResponse> recommendResponses = recClient.getRecommendation(searchRequest.getQuery(), TOP_K);
            aptInfos = new ArrayList<>();

            for (RecommendResponse recommendResponse : recommendResponses) {
                String bjdCode = addressRepository.selectBjdCodeByDanjiId(recommendResponse.getDanjiId());
                if (bjdCode == null) {
                    continue;
                }
                List<DanjiCode> danjiCodes = aptClient.getDanjiCodeList(bjdCode);
                List<AptInfo> unverifiedAptInfos = aptService.findAptInfoByDongCode(bjdCode);

                for (DanjiCode danjiCode : danjiCodes) {
                    System.out.println(danjiCode);
                    RoadAddressUtil.AddressComponents components = RoadAddressUtil.parseAddress(danjiCode.getRoadAddress());
                    for (AptInfo aptInfo : unverifiedAptInfos) {
                        if (searchRequest.getLocationFilter() == null || searchRequest.getLocationFilter().isEmpty() || !checkSameAddress(components, aptInfo)) continue;
                        aptInfos.add(aptInfo);
                        break;
                    }
                }
            }
        }

        // Apply filters
        aptInfos = filterAptInfosByOverlap(aptInfos, searchRequest);

        // Save search history for authenticated users
        if (authService.isAuthenticated()) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            historyService.addHistory(new HistoryRequest(username, searchRequest.getQuery()));
        }

        // Trim results if too many
        if (aptInfos.size() >= TOP_K) {
            return aptInfos.subList(0, aptInfos.size() / 2);
        }
        return aptInfos;
    }


    @Override
    public SearchResult viewSearched(String aptSeq) throws Exception {
        AptInfo aptInfo = aptRepository.selectAptInfoByAptSeq(aptSeq);
        List<DanjiCode> danjiCodes = aptClient.getDanjiCodeList(aptInfo.getSggCd() + aptInfo.getUmdCd());

        for (DanjiCode danjiCode : danjiCodes) {
            RoadAddressUtil.AddressComponents components = RoadAddressUtil.parseAddress(danjiCode.getRoadAddress());
            if (!checkSameAddress(components, aptInfo)) continue;
            return aptClient.getAptDetail(danjiCode.getKaptCode());
        }
        throw new Exception("viewSearched: No matching danji code found for apt seq: " + aptSeq);
    }



    private boolean checkSameAddress(RoadAddressUtil.AddressComponents components, AptInfo aptInfo) {
        return components.getRoadNm().equals(aptInfo.getRoadNm()) && components.getRoadNmBonbun().equals(aptInfo.getRoadNmBonbun()) && components.getRoadNmBubun().equals(aptInfo.getRoadNmBubun());
    }

    private List<AptInfo> filterAptInfosByOverlap(List<AptInfo> aptInfos, SearchRequest searchRequest) {
        return aptInfos.stream().filter(aptInfo -> {
            boolean matches = true;

            if (searchRequest.getMinPrice() != null || searchRequest.getMaxPrice() != null) {
                matches &= isOverlappingRange(
                        parseToInt(aptInfo.getMinDealAmount()),
                        parseToInt(aptInfo.getMaxDealAmount()),
                        searchRequest.getMinPrice(),
                        searchRequest.getMaxPrice()
                );
            }
            if (searchRequest.getMinArea() != null || searchRequest.getMaxArea() != null) {
                matches &= isOverlappingRange(
                        parseToDouble(aptInfo.getMinExcluUseAr()),
                        parseToDouble(aptInfo.getMaxExcluUseAr()),
                        searchRequest.getMinArea(),
                        searchRequest.getMaxArea()
                );
            }
            if (searchRequest.getLocationFilter() != null) {
                DongCode dongCode = locationRepository.selectDongCodeByDongcode(aptInfo.getSggCd() + aptInfo.getUmdCd());
                String startAddress = dongCode.getSidoName() + " " + dongCode.getGugunName() + " " + dongCode.getDongName();
                matches &= startAddress.startsWith(RoadAddressUtil.cleanAddress(searchRequest.getLocationFilter()));
            }
            return matches;
        }).toList();
    }

    private boolean isOverlappingRange(Number minApt, Number maxApt, Number minRequest, Number maxRequest) {
        if (minApt == null || maxApt == null) return false;

        // Default request range if null
        minRequest = minRequest != null ? minRequest : Double.MIN_VALUE;
        maxRequest = maxRequest != null ? maxRequest : Double.MAX_VALUE;

        // Overlap condition
        return minApt.doubleValue() <= maxRequest.doubleValue() && maxApt.doubleValue() >= minRequest.doubleValue();
    }

    private Integer parseToInt(String value) {
        try {
            return Integer.parseInt(value.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }

    private Double parseToDouble(String value) {
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return Double.MIN_VALUE;
        }
    }
}
