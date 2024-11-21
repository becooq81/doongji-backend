package com.find.doongji.listing.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListEntity {
    private String imagePath;
    private Long addressMappingId;
    private String aptDong;
    private String description;
    private String username;
}
