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

    @NotBlank(message = "리뷰에 아파트 번호는 필수 입력 값입니다.")
    private String aptSeq;

    @NotBlank(message = "리뷰 내용은 필수 입력 값입니다.")
    private String content;

    @Override
    public String toString() {
        return "ReviewCreateRequest{" +
                "aptSeq='" + aptSeq + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}