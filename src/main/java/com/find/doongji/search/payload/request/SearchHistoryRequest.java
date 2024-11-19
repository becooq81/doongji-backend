package com.find.doongji.search.payload.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchHistoryRequest {

    @Size(min = 1, message = "Username cannot be blank")
    @Size(max = 50, message = "Username must not exceed 50 characters")
    private String username;

    @Size(min = 1, message = "Search query cannot be blank")
    @Size(max = 255, message = "Search query must not exceed 255 characters")
    private String query;

    @Override
    public String toString() {
        return "SearchHistory{" +
                "username='" + username + '\'' +
                ", query='" + query + '\'' +
                '}';
    }
}
