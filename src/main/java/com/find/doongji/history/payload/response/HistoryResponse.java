package com.find.doongji.history.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryResponse {
    private Long id;
    private String username;
    private String query;
    private LocalDateTime createdAt;
}
