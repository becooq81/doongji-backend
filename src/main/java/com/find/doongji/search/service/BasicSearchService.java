package com.find.doongji.search.service;

import com.find.doongji.apt.client.AptDetailClient;
import com.find.doongji.apt.payload.response.DanjiCode;
import com.find.doongji.apt.payload.response.SearchResult;
import com.find.doongji.apt.repository.AptRepository;
import com.find.doongji.history.payload.request.HistoryRequest;
import com.find.doongji.history.service.HistoryService;
import com.find.doongji.search.client.RecommendClient;
import com.find.doongji.search.payload.request.SearchRequest;
import com.find.doongji.search.payload.response.RecommendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicSearchService implements SearchService {

    private final RecommendClient aiClient;
    private final AptDetailClient aptClient;
    private final AptRepository aptRepository;
    private final HistoryService historyService;

    private static final int TOP_K = 10;

    @Override
    @Transactional
    public List<SearchResult> search(SearchRequest searchRequest) throws Exception {
        List<RecommendResponse> recommendResponses = aiClient.getRecommendation(searchRequest.getQuery(), TOP_K);
        List<SearchResult> searchResults = new ArrayList<>();
        for (RecommendResponse recommendResponse : recommendResponses) {
            String bjdCode = aptRepository.selectBjdCodeByDanjiId(recommendResponse.getDanjiId());
            List<DanjiCode> danjiCodes = aptClient.getDanjiCodeList(bjdCode);
            for (DanjiCode danjiCode : danjiCodes) {
                SearchResult searchResult = aptClient.getAptDetail(danjiCode.getKaptCode());
                if (searchResult != null) {
                    searchResults.add(searchResult);
                    if (searchResults.size() >= TOP_K) {
                        break;
                    }
                }
            }
        }
        historyService.addHistory(new HistoryRequest(searchRequest.getUsername(), searchRequest.getQuery()));
        return searchResults;
    }
}
