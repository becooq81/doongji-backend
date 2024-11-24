package com.find.doongji.review.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewEntity {

    private Long danjiId;
    private String description;
    private String aptSeq;

}
