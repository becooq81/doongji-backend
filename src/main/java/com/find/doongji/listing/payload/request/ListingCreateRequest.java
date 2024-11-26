package com.find.doongji.listing.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingCreateRequest {

    @NotNull(message = "도로명 주소는 필수 입력 값입니다.")
    private String roadAddress;

    @NotNull(message = "지번 주소는 필수 입력 값입니다.")
    private String jibunAddress;

    private String aptDong;
    private String aptHo;
    private String description;

    @NotNull(message = "광각 분류 결과는 필수 입력 값입니다.")
    private Integer result;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    @Positive(message = "가격은 0보다 커야 합니다.")
    private Integer price;

    @Override
    public String toString() {
        return "ListingCreateRequest{" +
                "roadAddress='" + roadAddress + '\'' +
                ", jibunAddress='" + jibunAddress + '\'' +
                ", aptDong='" + aptDong + '\'' +
                ", aptHo='" + aptHo + '\'' +
                ", description='" + description + '\'' +
                ", result=" + result +
                ", price='" + price + '\'' +
                '}';
    }
}
