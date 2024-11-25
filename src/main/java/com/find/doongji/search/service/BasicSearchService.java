package com.find.doongji.search.service;

import com.find.doongji.address.payload.response.AddressMappingResponse;
import com.find.doongji.address.repository.AddressRepository;
import com.find.doongji.address.util.AddressUtil;
import com.find.doongji.apt.client.AptDetailClient;
import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.apt.repository.AptRepository;
import com.find.doongji.auth.service.AuthService;
import com.find.doongji.danji.payload.response.DanjiCode;
import com.find.doongji.danji.repository.DanjiRepository;
import com.find.doongji.history.payload.request.HistoryRequest;
import com.find.doongji.history.service.HistoryService;
import com.find.doongji.like.service.LikeService;
import com.find.doongji.location.payload.response.DongCode;
import com.find.doongji.location.repository.LocationRepository;
import com.find.doongji.review.service.ReviewService;
import com.find.doongji.search.client.RecommendClient;
import com.find.doongji.search.enums.SimilarityScore;
import com.find.doongji.search.payload.request.SearchQuery;
import com.find.doongji.search.payload.request.SearchRequest;
import com.find.doongji.search.payload.response.RecommendResponse;
import com.find.doongji.search.payload.response.SearchDetailResponse;
import com.find.doongji.search.payload.response.SearchResponse;
import com.find.doongji.search.payload.response.SearchResult;
import com.find.doongji.search.repository.SearchRepository;
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

    private static final int TOP_K = 15000;

    private final RecommendClient recClient;
    private final AptDetailClient aptClient;

    private final AddressRepository addressRepository;
    private final AptRepository aptRepository;
    private final LocationRepository locationRepository;
    private final DanjiRepository danjiRepository;
    private final SearchRepository searchRepository;

    private final HistoryService historyService;
    private final AuthService authService;
    private final ReviewService reviewService;
    private final LikeService likeService;

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Override
    @Transactional
    public List<SearchResponse> search(SearchRequest searchRequest, int page, int size) throws Exception {
        List<AptInfo> aptInfos;
        page--;

        int totalSize;
        int startIndex = page * size;
        int endIndex = startIndex + size;

        if (searchRequest.getQuery() == null || searchRequest.getQuery().trim().isEmpty()) {
            SearchQuery query = SearchQuery.builder()
                    .minPrice(searchRequest.getMinPrice())
                    .maxPrice(searchRequest.getMaxPrice())
                    .locationFilter(searchRequest.getLocationFilter())
                    .minArea(searchRequest.getMinArea())
                    .maxArea(searchRequest.getMaxArea())
                    .offset(startIndex)
                    .size(size)
                    .build();
            aptInfos = searchRepository.filterBySearchQuery(query);

            totalSize = aptInfos.size();
            endIndex = Math.min(endIndex, totalSize);

            List<AptInfo> paginatedAptInfos = aptInfos.subList(startIndex, endIndex);

            List<SearchResponse> responses = paginatedAptInfos.stream().map(
                    aptInfo -> new SearchResponse(
                            SimilarityScore.NONE,
                            aptInfo,
                            likeService.viewLike(aptInfo.getAptSeq()),
                            totalSize
                    )
            ).toList();

            return responses;

        } else {
            List<RecommendResponse> recommendResponses = recClient.getRecommendation(searchRequest.getQuery(), TOP_K).stream()
                    .filter(distinctByKey(RecommendResponse::getDanjiId))
                    .toList();

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

            Map<String, Float> similarityMap = aptSeqToDanjiIdsMap.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> {
                                List<Long> danjiIdList = entry.getValue();
                                return danjiIdList.stream()
                                        .map(danjiId -> recommendResponses.stream()
                                                .filter(response -> response.getDanjiId().equals(danjiId))
                                                .findFirst()
                                                .map(RecommendResponse::getSimilarity)
                                                .orElse(0.0f))
                                        .max(Float::compare)
                                        .orElse(0.0f);
                            }
                    ));

            List<String> aptSeqList = mappings.stream()
                    .map(AddressMappingResponse::getAptSeq)
                    .collect(Collectors.toList());

            SearchQuery query = SearchQuery.builder()
                    .aptSeqList(aptSeqList)
                    .minPrice(searchRequest.getMinPrice())
                    .maxPrice(searchRequest.getMaxPrice())
                    .locationFilter(searchRequest.getLocationFilter())
                    .minArea(searchRequest.getMinArea())
                    .maxArea(searchRequest.getMaxArea())
                    .offset(startIndex)
                    .size(size)
                    .build();
            aptInfos = searchRepository.filterBySearchQuery(query);

            Map<String, Double> aptSeqToMaxSimilarity = aptInfos.stream()
                    .collect(Collectors.toMap(
                            AptInfo::getAptSeq,
                            aptInfo -> {
                                List<Long> danjiIdList = aptSeqToDanjiIdsMap.getOrDefault(aptInfo.getAptSeq(), List.of());
                                return danjiIdList.stream()
                                        .map(recommendResponseMap::get)
                                        .filter(Objects::nonNull)
                                        .mapToDouble(RecommendResponse::getSimilarity)
                                        .max()
                                        .orElse(0.0);
                            }
                    ));

            aptInfos = aptInfos.stream()
                    .sorted(Comparator.comparingDouble(aptInfo -> -similarityMap.get(aptInfo.getAptSeq())))
                    .toList();

            List<SearchResponse> searchResponses = aptInfos.stream()
                    .map(aptInfo -> {
                        float similarityScore = similarityMap.getOrDefault(aptInfo.getAptSeq(), 0.0f);
                        SimilarityScore similarity = SimilarityScore.classify(similarityScore);
                        return new SearchResponse(
                                similarity,
                                aptInfo,
                                likeService.viewLike(aptInfo.getAptSeq()),
                                0
                        );
                    })
                    .toList();

            trackSearchHistory(searchRequest);

            return searchResponses;
        }
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
                .overview(reviewService.getReviewsByAptSeq(aptSeq))
                .build();
    }


    private void trackSearchHistory(SearchRequest searchRequest) {
        if (authService.isAuthenticated() && searchRequest.getQuery() != null) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            historyService.addHistory(new HistoryRequest(username, searchRequest.getQuery()));
        }
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
