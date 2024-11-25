package com.find.doongji.search.repository;

import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.search.payload.request.SearchQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class SearchRepositoryTest {

    @Autowired
    private SearchRepository searchRepository;

    @Test
    public void testFilterBySearchQuery_WithAllFilters() {
        SearchQuery searchQuery = SearchQuery.builder()
                .aptSeqList(List.of("41210-3461", "41210-3463", "41210-3478"))
                .minPrice(140)
                .maxPrice(50000000)
                .minArea(20.5)
                .maxArea(60.74)
                .size(10)
                .offset(0)
                .build();

        List<AptInfo> results = searchRepository.filterBySearchQuery(searchQuery);

        assertThat(results).isNotEmpty();
        AptInfo aptInfo = results.get(0);
        System.out.println(aptInfo);
    }

    @Test
    public void testFilterBySearchQuery_WithPartialFilters() {
        SearchQuery searchQuery = SearchQuery.builder()
                .aptSeqList(List.of("41210-3461", "41210-3463", "41210-3478"))
                .minArea(20.5)
                .maxArea(60.74)
                .locationFilter("경기도 광명시 일직동")
                .size(10)
                .offset(0)
                .build();
        List<AptInfo> results = searchRepository.filterBySearchQuery(searchQuery);

        assertThat(results).isNotEmpty();
        AptInfo aptInfo = results.get(0);
        System.out.println(aptInfo);
    }

}
