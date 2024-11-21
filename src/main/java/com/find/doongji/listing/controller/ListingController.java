package com.find.doongji.listing.controller;

import com.find.doongji.listing.payload.request.ListingCreateRequest;
import com.find.doongji.listing.payload.request.ListingUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ListingController {

    ResponseEntity<?> createListing(MultipartFile image, ListingCreateRequest request);
    ResponseEntity<?> updateListing(ListingUpdateRequest request);
    ResponseEntity<?> deleteListing(Long id);
    ResponseEntity<?> getListing(Long id);
    ResponseEntity<?> getListings(String address);

}
