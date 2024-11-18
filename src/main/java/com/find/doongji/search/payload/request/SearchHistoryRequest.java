package com.find.doongji.search.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchHistoryRequest {

    private String username;
    private String query;

    @Override
    public String toString() {
        return "SearchHistory{" +
                "username='" + username + '\'' +
                ", query='" + query + '\'' +
                '}';
    }
}
