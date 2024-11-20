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
    private MultipartFile image;

    // TODO: 프론트단에서 도로명주소인지 구주소인지 구분할 수 있나요?
    @NotNull
    private String address; // 도로명주소 또는 구주소 중 하나

    private String aptDong;

    private String description;

}
