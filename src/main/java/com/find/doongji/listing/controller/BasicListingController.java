package com.find.doongji.listing.controller;

import com.find.doongji.listing.payload.request.ListingCreateRequest;
import com.find.doongji.listing.payload.request.ListingUpdateRequest;
import com.find.doongji.listing.service.ListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listing")
public class BasicListingController implements ListingController {

    private final ListingService listingService;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<?> createListing(@RequestBody @Valid ListingCreateRequest request) {
        return null;
    }

    @Override
    @PutMapping
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<?> updateListing(@RequestBody @Valid ListingUpdateRequest request) {
        return null;
    }

    @Override
    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<?> deleteListing(@RequestParam("id") Long id) {
        return null;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> getListing(@RequestParam("id") Long id) {
        return null;
    }

    @Override
    @GetMapping("/{address}")
    public ResponseEntity<?> getListings(@RequestParam("address") String address) {
        return null;
    }


}
