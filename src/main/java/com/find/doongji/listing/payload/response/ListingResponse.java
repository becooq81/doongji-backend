package com.find.doongji.listing.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
public class ListingResponse {
    private Long id;
    private Long addressMappingId;
    private String username;

    @Setter
    private String imagePath;
    private int isOptical;

    private String oldAddress;
    private String roadAddress;

    private String aptDong;
    private String aptHo;

    private String description;

    private Integer price;

}
