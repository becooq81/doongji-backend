package com.find.doongji.search.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    private String query;

    // Filter
    private Integer minPrice = Integer.MIN_VALUE;
    private Integer maxPrice = Integer.MAX_VALUE;
    private String locationFilter;
    private Double minArea = Double.MIN_VALUE;
    private Double maxArea = Double.MAX_VALUE;
}
