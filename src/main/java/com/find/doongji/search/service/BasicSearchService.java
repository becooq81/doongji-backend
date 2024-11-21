package com.find.doongji.search.service;

import com.find.doongji.address.repository.AddressRepository;
import com.find.doongji.address.util.RoadAddressUtil;
import com.find.doongji.apt.client.AptDetailClient;
import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.apt.payload.response.DanjiCode;
import com.find.doongji.apt.service.AptService;
import com.find.doongji.auth.service.AuthService;
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

    private final HistoryService historyService;
    private final AptService aptService;
    private final AuthService authService;

    private static final int TOP_K = 1000;

    @Override
    @Transactional
    public List<AptInfo> search(SearchRequest searchRequest) throws Exception {
        List<RecommendResponse> recommendResponses = recClient.getRecommendation(searchRequest.getQuery(), TOP_K);
        List<AptInfo> aptInfos = new ArrayList<>();

        for (RecommendResponse recommendResponse : recommendResponses) {
            String bjdCode = addressRepository.selectBjdCodeByDanjiId(recommendResponse.getDanjiId());
            if (bjdCode == null) {
                continue; // INFO: 공개된 공동주택에 대한 정보만 관리하는 API를 사용하기 때문에 존재하지 않을 수도 있음
            }
            List<DanjiCode> danjiCodes = aptClient.getDanjiCodeList(bjdCode);
            List<AptInfo> unverifiedAptInfos = aptService.findAptInfoByDongCode(bjdCode);
            for (DanjiCode danjiCode : danjiCodes) {
                RoadAddressUtil.AddressComponents components = RoadAddressUtil.parseAddress(danjiCode.getRoadAddress());
                for (AptInfo aptInfo : unverifiedAptInfos) {
                    if (!checkSameAddress(components, aptInfo)) continue;
                    aptInfos.add(aptInfo);
                    break;
                }
            }
        }

        if (authService.isAuthenticated()) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            historyService.addHistory(new HistoryRequest(username, searchRequest.getQuery()));
        }

        if (aptInfos.size() >= 1000) {
            return aptInfos.subList(0, aptInfos.size()/2);
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
}
