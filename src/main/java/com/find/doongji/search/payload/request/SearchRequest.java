package com.find.doongji.search.payload.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    @Size(min = 1, max = 255, message = "Search query must be between 1 and 255 characters")
    private String query;

}
