package com.find.doongji.search.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendResponse {
    private int danjiId;
    private float similarity;

    @Override
    public String toString() {
        return "RecommendResponse{" +
                "danjiId=" + danjiId +
                ", similarity=" + similarity +
                '}';
    }
}
