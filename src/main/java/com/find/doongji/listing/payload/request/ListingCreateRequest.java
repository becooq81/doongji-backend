package com.find.doongji.listing.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingCreateRequest {

    @NotNull
    private String roadAddress;

    @NotNull
    private String jibunAddress;

    private String aptDong;
    private String aptHo;
    private String description;

    @NotNull
    private int result;


    @Override
    public String toString() {
        return "ListingCreateRequest{" +
                "roadAddress='" + roadAddress + '\'' +
                ", jibunAddress='" + jibunAddress + '\'' +
                ", aptDong='" + aptDong + '\'' +
                ", aptHo='" + aptHo + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
