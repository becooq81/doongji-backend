package com.find.doongji.search.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchHistoryRequest {

    @NotBlank(message = "Username cannot be blank")
    @Size(max = 50, message = "Username must not exceed 50 characters")
    private String username;

    @NotBlank(message = "Search query cannot be blank")
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
