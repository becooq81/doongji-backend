package com.find.doongji.listing.service;

import com.find.doongji.auth.service.AuthService;
import com.find.doongji.listing.payload.request.ListingCreateRequest;
import com.find.doongji.listing.payload.request.ListingUpdateRequest;
import com.find.doongji.listing.payload.response.ListingResponse;
import com.find.doongji.listing.repository.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicListingService implements ListingService {

    private final ListingRepository listingRepository;
    private final AuthService authService;

    @Override
    public void addListing(ListingCreateRequest request) throws IllegalArgumentException{

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

    }

    @Override
    public void removeListing(Long id) {

    }

    @Override
    public void updateListing(ListingUpdateRequest request) {

    }

    @Override
    public ListingResponse getListing(Long id) {
        return null;
    }

    @Override
    public List<ListingResponse> getListings(String address) {
        return List.of();
    }
}
