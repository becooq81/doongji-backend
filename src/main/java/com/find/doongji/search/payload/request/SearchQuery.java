package com.find.doongji.search.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchQuery {

    private List<String> aptSeqList;

    // Filter
    private Integer minPrice = Integer.MIN_VALUE;
    private Integer maxPrice = Integer.MAX_VALUE;
    private String locationFilter;
    private Double minArea = Double.MIN_VALUE;
    private Double maxArea = Double.MAX_VALUE;

    private int offset;
    private int size;
}
