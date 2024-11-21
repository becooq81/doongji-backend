package com.find.doongji.listing.service;

import com.find.doongji.address.payload.request.AddressMapping;
import com.find.doongji.address.payload.response.AddressMappingResponse;
import com.find.doongji.address.repository.AddressRepository;
import com.find.doongji.address.util.RoadAddressUtil;
import com.find.doongji.apt.client.AptClient;
import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.apt.payload.response.DanjiCode;
import com.find.doongji.apt.repository.AptRepository;
import com.find.doongji.auth.service.AuthService;
import com.find.doongji.listing.client.ClassificationClient;
import com.find.doongji.listing.payload.request.ListingCreateRequest;
import com.find.doongji.listing.payload.request.ListingEntity;
import com.find.doongji.listing.payload.request.ListingUpdateEntity;
import com.find.doongji.listing.payload.request.ListingUpdateRequest;
import com.find.doongji.listing.payload.response.ClassificationResponse;
import com.find.doongji.listing.payload.response.ListingResponse;
import com.find.doongji.listing.repository.ListingRepository;
import com.find.doongji.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicListingService implements ListingService {

    private final ListingRepository listingRepository;
    private final AptRepository aptRepository;
    private final AddressRepository addressRepository;
    private final LocationRepository locationRepository;

    private final AptClient aptClient;
    private final ClassificationClient classificationClient;

    @Override
    public void addListing(ListingCreateRequest request, MultipartFile image) throws Exception {

        ClassificationResponse classificationResponse = classificationClient.classify(image, "./uploads");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<AddressMappingResponse> addressMappings = addressRepository.selectAddressMappingByRoadAddress(request.getRoadAddress());

        List<Long> ids = new ArrayList<>();
        if (addressMappings.isEmpty()) {
            ids.add(createAddressMapping(request));
        } else {
            for (AddressMappingResponse addressMapping : addressMappings) {
                ids.add(addressMapping.getId());
            }
        }

        for (Long id : ids) {
            ListingEntity entity = ListingEntity.builder()
                            .addressMappingId(id)
                            .username(username)
                            .imagePath(classificationResponse.getImagePath())
                            .isOptical(classificationResponse.getResult())
                            .oldAddress(request.getJibunAddress())
                            .roadAddress(request.getRoadAddress())
                            .aptDong(request.getAptDong())
                            .aptHo(request.getAptHo())
                            .description(request.getDescription())
                                    .build();
            listingRepository.insertListing(entity);
        }
    }

    @Override
    public void removeListing(Long id) {
        if (!checkEditPermission(id)) {
            throw new IllegalArgumentException("해당 매물을 삭제할 권한이 없습니다.");
        }
        listingRepository.deleteListing(id);
    }

    @Override
    public void updateListing(ListingUpdateRequest request) {
        if (!checkEditPermission(request.getId())) {
            throw new IllegalArgumentException("해당 매물을 수정할 권한이 없습니다.");
        }

        ListingResponse response = listingRepository.selectListing(request.getId());
        if (response == null) {
            throw new IllegalArgumentException("Listing not found for ID: " + request.getId());
        }

        String roadAddress = request.getRoadAddress() == null ? response.getRoadAddress() : request.getRoadAddress();

        ListingUpdateEntity entity = ListingUpdateEntity.builder()
                .id(request.getId())
                .addressMappingId(response.getAddressMappingId())
                .username(response.getUsername())
                .imagePath(response.getImagePath())
                .oldAddress(request.getOldAddress() == null ? response.getOldAddress() : request.getOldAddress())
                .roadAddress(roadAddress)
                .aptDong(request.getAptDong() == null ? response.getAptDong() : request.getAptDong())
                .aptHo(request.getAptHo() == null ? response.getAptHo() : request.getAptHo())
                .description(request.getDescription() == null ? response.getDescription() : request.getDescription())
                .build();

        listingRepository.updateListing(entity);

    }

    @Override
    public ListingResponse getListing(Long id) {
        return listingRepository.selectListing(id);
    }

    @Override
    public List<ListingResponse> getListingsForRoadAddress(String roadAddress) {
        String cleanedAddress = RoadAddressUtil.cleanAddress(roadAddress);
        return listingRepository.selectListingsByRoadAddress(cleanedAddress);
    }

    private String getDongCode(String oldAddress) {
        String[] parts = oldAddress.split("\\s+");
        if (parts.length < 3) {
            throw new IllegalArgumentException("지번주소 형식이 잘못되었습니다.");
        }
        String siDo = parts[0];           // 시도
        String siGunGu = parts[1];       // 시군구
        String eupMyeonDongRi = parts[2]; // 읍면동/리
        return locationRepository.selectDongCode(siDo, siGunGu, eupMyeonDongRi);
    }

    private Long createAddressMapping(ListingCreateRequest request) throws Exception {
        String bjdCode = getDongCode(request.getJibunAddress());
        List<DanjiCode> danjiCodes = aptClient.getDanjiCodeList(bjdCode);

        RoadAddressUtil.AddressComponents components = RoadAddressUtil.parseAddress(request.getRoadAddress());

        System.out.println("road_nm: " + components.getRoadNm()+", road_nm_bonbun: "+components.getRoadNmBonbun()+", road_nm_bubun: "+components.getRoadNmBubun());

        // TODO: 해당 레포지토리 메서드는 단일 레코드가 아닌 집합을 리턴하기 때문에 모두 고려해야 한다
        AptInfo aptInfo = aptRepository.selectAptInfoByRoadComponents(components.getRoadNm(), components.getRoadNmBonbun(), components.getRoadNmBubun()).get(0);

        Long id = -1L;
        for (DanjiCode danjiCode : danjiCodes) {
            if (!request.getRoadAddress().startsWith(danjiCode.getRoadAddress())) continue;
            id = addressRepository.insertAddressMapping(
                    AddressMapping.builder()
                            .roadAddress(request.getRoadAddress())
                            .oldAddress(request.getJibunAddress())
                            .umdNm(aptInfo.getUmdNm())
                            .jibun(aptInfo.getJibun())
                            .aptSeq(aptInfo.getAptSeq())
                            .bjdCode(danjiCode.getBjdCode())
                            .roadNm(components.getRoadNm())
                            .roadNmBonbun(components.getRoadNmBonbun())
                            .roadNmBubun(components.getRoadNmBubun()).build()
            );
            break;
        }
        return id;
    }

    private boolean checkEditPermission(Long id) {
        // authentication 여부는 컨트롤러 단에서 확인한다
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        ListingResponse listing = listingRepository.selectListing(id);
        return listing.getUsername().equals(username);
    }

}
