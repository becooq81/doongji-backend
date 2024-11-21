package com.find.doongji.search.service;

import com.find.doongji.address.repository.AddressRepository;
import com.find.doongji.apt.client.AptDetailClient;
import com.find.doongji.apt.payload.response.DanjiCode;
import com.find.doongji.auth.service.AuthService;
import com.find.doongji.search.payload.response.SearchResponse;
import com.find.doongji.search.payload.response.SearchResult;
import com.find.doongji.apt.repository.AptRepository;
import com.find.doongji.history.payload.request.HistoryRequest;
import com.find.doongji.history.service.HistoryService;
import com.find.doongji.search.client.RecommendClient;
import com.find.doongji.search.payload.request.SearchRequest;
import com.find.doongji.search.payload.response.RecommendResponse;
import com.find.doongji.search.enums.SimilarityScore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicSearchService implements SearchService {

    private final RecommendClient aiClient;
    private final AptDetailClient aptClient;

    private final AddressRepository addressRepository;

    private final HistoryService historyService;
    private final AuthService authService;

    private static final int TOP_K = 10;

    @Override
    @Transactional
    public List<SearchResponse> search(SearchRequest searchRequest) throws Exception {
        List<RecommendResponse> recommendResponses = aiClient.getRecommendation(searchRequest.getQuery(), TOP_K);
        List<SearchResponse> searchResults = new ArrayList<>();


        outerLoop:
        for (RecommendResponse recommendResponse : recommendResponses) {
            String bjdCode = addressRepository.selectBjdCodeByDanjiId(recommendResponse.getDanjiId());
            if (bjdCode == null) {
                continue; // INFO: 공개된 공동주택에 대한 정보만 관리하는 API를 사용하기 때문에 존재하지 않을 수도 있음
            }
            List<DanjiCode> danjiCodes = aptClient.getDanjiCodeList(bjdCode);
            for (DanjiCode danjiCode : danjiCodes) {
                System.out.println(danjiCode.getKaptCode());
                SearchResult searchResult = aptClient.getAptDetail(danjiCode.getKaptCode());
                System.out.println(searchResult);
                if (searchResult != null) {
                    searchResults.add(new SearchResponse(searchResult, SimilarityScore.classify(recommendResponse.getSimilarity())));
                    if (searchResults.size() >= TOP_K) {
                        break outerLoop;
                    }
                }
            }
        }
        if (authService.isAuthenticated()) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            historyService.addHistory(new HistoryRequest(username, searchRequest.getQuery()));
        }
        return searchResults;
    }
}
