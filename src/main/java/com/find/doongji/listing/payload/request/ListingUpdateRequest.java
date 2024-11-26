package com.find.doongji.listing.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ListingUpdateRequest {

    @NotBlank
    private Long id;

    private String oldAddress;
    private String roadAddress;

    private String aptDong;
    private String aptHo;
    private String description;

    private Integer price;


}
