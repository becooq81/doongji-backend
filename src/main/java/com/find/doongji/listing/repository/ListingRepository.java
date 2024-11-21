package com.find.doongji.listing.repository;

import com.find.doongji.listing.payload.request.ListingEntity;
import com.find.doongji.listing.payload.request.ListingUpdateEntity;
import com.find.doongji.listing.payload.response.ListingResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ListingRepository {

    /**
     * 매물 등록
     * @param listingEntity
     */
    void insertListing(ListingEntity listingEntity);

    /**
     * 매물 조회
     * @param addressMappingId
     * @return ListingResponse
     */
    ListingResponse selectListing(Long addressMappingId);

    /**
     * 매물 삭제
     * @param id
     */
    void deleteListing(Long id);

    /**
     * 매물 수정
     */
    void updateListing(ListingUpdateEntity listingEntity);

    /**
     * 도로명 주소로 매물 조회
     * @param roadAddress
     */
    List<ListingResponse> selectListingsByRoadAddress(String roadAddress);
}
