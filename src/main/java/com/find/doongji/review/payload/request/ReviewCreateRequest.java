package com.find.doongji.review.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReviewCreateRequest {

    @NotBlank
    private String aptSeq;

    @NotBlank
    private String content;

    @Override
    public String toString() {
        return "ReviewCreateRequest{" +
                "aptSeq='" + aptSeq + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}