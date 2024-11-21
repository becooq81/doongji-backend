package com.find.doongji.listing.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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

}
