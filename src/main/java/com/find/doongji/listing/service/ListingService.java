package com.find.doongji.listing.service;

import com.find.doongji.listing.payload.request.ListingCreateRequest;
import com.find.doongji.listing.payload.request.ListingUpdateRequest;
import com.find.doongji.listing.payload.response.ListingResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ListingService {

    /**
     * 매물 등록
     *
     * @param request
     */
    void addListing(ListingCreateRequest request, MultipartFile image) throws Exception;

    /**
     * 매물 삭제
     *
     * @param id
     */
    void removeListing(Long id);

    /**
     * 매물 수정
     *
     * @param request
     */
    void updateListing(ListingUpdateRequest request);

    /**
     * 매물 조회
     *
     * @param id
     */
    ListingResponse getListing(Long id);

    /**
     * 매물 목록 조회
     *
     * @param address
     */
    List<ListingResponse> getListingsForRoadAddress(String address);

}
