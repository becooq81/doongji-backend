package com.find.doongji.listing.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingUpdateEntity {
    private Long id;
    private Long addressMappingId;
    private String username;

    private String imagePath;
    private int isOptical;
    private String oldAddress;
    private String roadAddress;

    private String aptDong;
    private String aptHo;
    private String description;

    private String price;

}
