package com.find.doongji.search.payload.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    @Size(min = 1, message = "Username cannot be blank")
    private String username;

    @Size(min = 1, message = "Search query cannot be blank")
    private String query;

}
