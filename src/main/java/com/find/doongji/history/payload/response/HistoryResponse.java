package com.find.doongji.search.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchHistoryResponse {
    private Long id;
    private String username;
    private String query;
    private LocalDateTime createdAt;
}
