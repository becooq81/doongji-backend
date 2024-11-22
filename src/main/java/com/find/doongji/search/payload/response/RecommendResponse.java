package com.find.doongji.search.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendResponse {
    private Long danjiId;
    private float similarity;

    @Override
    public String toString() {
        return "RecommendResponse{" +
                "danjiId=" + danjiId +
                ", similarity=" + similarity +
                '}';
    }
}
