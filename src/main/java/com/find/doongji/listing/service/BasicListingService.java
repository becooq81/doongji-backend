package com.find.doongji.listing.service;

import com.find.doongji.address.payload.request.AddressMapping;
import com.find.doongji.address.payload.response.AddressMappingResponse;
import com.find.doongji.address.repository.AddressRepository;
import com.find.doongji.address.util.AddressUtil;
import com.find.doongji.apt.client.AptClient;
import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.apt.repository.AptRepository;
import com.find.doongji.danji.payload.response.DanjiCode;
import com.find.doongji.danji.repository.DanjiRepository;
import com.find.doongji.listing.payload.request.ListingCreateRequest;
import com.find.doongji.listing.payload.request.ListingEntity;
import com.find.doongji.listing.payload.request.ListingUpdateEntity;
import com.find.doongji.listing.payload.request.ListingUpdateRequest;
import com.find.doongji.listing.payload.response.ListingResponse;
import com.find.doongji.listing.repository.ListingRepository;
import com.find.doongji.listing.util.FileUploadUtil;
import com.find.doongji.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    private final DanjiRepository danjiRepository;

    @Value("${server.url}")
    private String serverUrl;

    @Override
    public void addListing(ListingCreateRequest request, MultipartFile image) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<AddressMappingResponse> addressMappings = addressRepository.selectAddressMappingByRoadAddress(request.getRoadAddress());

        List<Long> danjiIds = new ArrayList<>();
        if (addressMappings.isEmpty()) {
            danjiIds.add(createAddressMapping(request));
        } else {
            for (AddressMappingResponse addressMapping : addressMappings) {
                danjiIds.add(addressMapping.getDanjiId());
            }
        }

        String imagePath = FileUploadUtil.uploadFile(image, "./uploads");

        for (Long id : danjiIds) {
            AddressMappingResponse mapping = addressRepository.selectAddressMappingByDanjiId(id);
            ListingEntity entity = ListingEntity.builder()
                    .addressMappingId(mapping.getId())
                    .username(username)
                    .imagePath(cleanPath(removeUploadsPrefix(imagePath)))
                    .isOptical(request.getResult())
                    .oldAddress(request.getJibunAddress())
                    .roadAddress(AddressUtil.cleanAddress(request.getRoadAddress()))
                    .aptDong(request.getAptDong())
                    .aptHo(request.getAptHo())
                    .description(request.getDescription())
                    .price(request.getPrice())
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
                .price(request.getPrice() == null ? response.getPrice() : request.getPrice())
                .build();

        listingRepository.updateListing(entity);

    }

    @Override
    public ListingResponse getListing(Long id) {
        return listingRepository.selectListing(id);
    }

    @Override
    public List<ListingResponse> getListingsForRoadAddress(String roadAddress) {
        String cleanedAddress = AddressUtil.cleanAddress(roadAddress);

        List<ListingResponse>  responses =  listingRepository.selectListingsByRoadAddress(cleanedAddress);
        responses.forEach(listing -> listing.setImagePath(getFullImageUrl(listing.getImagePath())));
        return responses;
    }

    private String getFullImageUrl(String imagePath) {
        if (serverUrl == null || serverUrl.isEmpty()) {
            throw new IllegalArgumentException("Server URL cannot be null or empty");
        }
        if (imagePath == null) {
            return null;
        }

        // Normalize and encode the image path
        String normalizedServerUrl = serverUrl.endsWith("/")
                ? serverUrl.substring(0, serverUrl.length() - 1)
                : serverUrl;
        String normalizedImagePath = imagePath.startsWith("/")
                ? imagePath.substring(1)
                : imagePath;

        String encodedPath = URLEncoder.encode(normalizedImagePath.replace("\\", "/"), StandardCharsets.UTF_8);
        String result =  normalizedServerUrl + "/" + encodedPath;
        return result;
    }
    public String removeUploadsPrefix(String path) {
        if (path == null) return null;
        return path.replaceFirst("^\\.\\/uploads\\\\?", "");
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
        List<DanjiCode> danjiCodes = danjiRepository.selectAllByBjdCode(bjdCode);

        AddressUtil.AddressComponents components = AddressUtil.parseAddress(request.getRoadAddress());

        // TODO: 해당 레포지토리 메서드는 단일 레코드가 아닌 집합을 리턴하기 때문에 모두 고려해야 한다
        List<AptInfo> aptInfoList = aptRepository.selectAptInfoByRoadComponents(components.getRoadNm(), components.getRoadNmBonbun(), components.getRoadNmBubun());
        if (aptInfoList.isEmpty()) {
            throw new Exception("해당 도로명주소에 대한 아파트 정보가 존재하지 않습니다.");
        }
        AptInfo aptInfo = aptInfoList.get(0);
        Long danjiId = null;
        for (DanjiCode danjiCode : danjiCodes) {
            danjiId = (aptInfo.getAptSeq() + danjiCode.getKaptCode()).hashCode() & 0xffffffffL;
            if (!AddressUtil.cleanAddress(request.getJibunAddress()).startsWith(AddressUtil.cleanAddress(danjiCode.getSiGugunDong())))
                continue;
            AddressUtil.OldAddressComponents oldAddressComponents = AddressUtil.parseOldAddress(request.getJibunAddress());
            addressRepository.insertAddressMapping(
                    AddressMapping.builder()
                            .roadAddress(AddressUtil.cleanAddress(request.getRoadAddress()))
                            .oldAddress(oldAddressComponents.getJibunAddress())
                            .umdNm(aptInfo.getUmdNm())
                            .jibun(aptInfo.getJibun())
                            .aptSeq(aptInfo.getAptSeq())
                            .bjdCode(danjiCode.getBjdCode())
                            .roadNm(components.getRoadNm())
                            .roadNmBonbun(components.getRoadNmBonbun())
                            .roadNmBubun(components.getRoadNmBubun())
                            .danjiId(danjiId)
                            .aptNm(oldAddressComponents.getAptName())
                            .build()
            );
            break;
        }
        return danjiId;
    }

    private boolean checkEditPermission(Long id) {
        // authentication 여부는 컨트롤러 단에서 확인한다
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        ListingResponse listing = listingRepository.selectListing(id);
        return listing.getUsername().equals(username);
    }

    private String cleanPath(String path) {
        path.replaceAll(" ", "_");
        return path;
    }

}
