package com.find.doongji.listing.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingUpdateRequest {

    @NotBlank
    private Long id;

    private MultipartFile image;
    private String address;
    private String aptDong;
    private String description;

}
