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
        long startTime = System.nanoTime(); // Start overall timer
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
            long filterStart = System.nanoTime(); // Timer for filtering
            aptInfos = searchRepository.filterBySearchQuery(query);
            long filterEnd = System.nanoTime();
            System.out.println("Filter overlap took: " + (filterEnd - filterStart) / 1_000_000 + " ms");

            totalSize = aptInfos.size();
            endIndex = Math.min(endIndex, totalSize);

            if (startIndex >= totalSize) {
                System.out.println("Empty result due to pagination. Total size: " + totalSize);
                return new ArrayList<>();
            }

            List<AptInfo> paginatedAptInfos = aptInfos.subList(startIndex, endIndex);

            long responseStart = System.nanoTime(); // Timer for response mapping
            List<SearchResponse> responses = paginatedAptInfos.stream().map(
                    aptInfo -> new SearchResponse(
                            SimilarityScore.NONE,
                            aptInfo,
                            likeService.viewLike(aptInfo.getAptSeq()),
                            totalSize
                    )
            ).toList();
            long responseEnd = System.nanoTime();
            System.out.println("Mapping responses took: " + (responseEnd - responseStart) / 1_000_000 + " ms");

            return responses;

        } else {
            long recommendationStart = System.nanoTime(); // Timer for recommendation client
            List<RecommendResponse> recommendResponses = recClient.getRecommendation(searchRequest.getQuery(), TOP_K).stream()
                    .filter(distinctByKey(RecommendResponse::getDanjiId))
                    .toList();
            long recommendationEnd = System.nanoTime();
            List<Long> answers = new ArrayList<>();
            answers.add(37367L);
            answers.add(38391L);
            answers.add(36751L);
            answers.add(36969L);
            System.out.println("Recommendation fetch took: " + (recommendationEnd - recommendationStart) / 1_000_000 + " ms");

            long mappingStart = System.nanoTime();
            Map<Long, RecommendResponse> recommendResponseMap = recommendResponses.stream()
                    .collect(Collectors.toMap(RecommendResponse::getDanjiId, Function.identity()));

            List<Long> danjiIds = new ArrayList<>(recommendResponseMap.keySet());
            for (Long danjiId : danjiIds) {
                if (danjiId==1035L) {
                    System.out.println("danjiId: " + danjiId);
                }
            }
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
            long mappingEnd = System.nanoTime();
            System.out.println("Mapping danji IDs took: " + (mappingEnd - mappingStart) / 1_000_000 + " ms");

            long similarityStart = System.nanoTime(); // Timer for similarity map creation
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
            long similarityEnd = System.nanoTime();
            System.out.println("Building similarity map took: " + (similarityEnd - similarityStart) / 1_000_000 + " ms");
            System.out.println("PAGE: " + page + " SIZE: " + size);

            long filteringStart = System.nanoTime(); // Timer for filtering aptInfos
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
            long filteringEnd = System.nanoTime();
            System.out.println("Filtering aptInfos took: " + (filteringEnd - filteringStart) / 1_000_000 + " ms");

            long sortingStart = System.nanoTime(); // Timer for sorting
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
                    .sorted(Comparator.comparingDouble(aptInfo -> -aptSeqToMaxSimilarity.getOrDefault(aptInfo.getAptSeq(), 0.0)))
                    .toList();
            long sortingEnd = System.nanoTime();
            System.out.println("Sorting aptInfos took: " + (sortingEnd - sortingStart) / 1_000_000 + " ms");

            long responseStart = System.nanoTime(); // Timer for response mapping
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
            long responseEnd = System.nanoTime();
            System.out.println("Mapping responses took: " + (responseEnd - responseStart) / 1_000_000 + " ms");

            trackSearchHistory(searchRequest);
            System.out.println("Total execution time: " + (System.nanoTime() - startTime) / 1_000_000 + " ms");

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
        return checkLocationFilter(aptInfo, searchRequest);
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
