package com.find.doongji.address.service;

import com.find.doongji.address.payload.request.AddressMapping;
import com.find.doongji.address.repository.AddressRepository;
import com.find.doongji.address.service.AddressService;
import com.find.doongji.address.util.AddressUtil;
import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.apt.repository.AptRepository;
import com.find.doongji.danji.payload.response.DanjiCode;
import com.find.doongji.danji.repository.DanjiRepository;
import com.find.doongji.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAddressService implements AddressService {

    private final LocationRepository locationRepository;
    private final DanjiRepository danjiRepository;
    private final AptRepository aptRepository;
    private final AddressRepository addressRepository;

    @Override
    public Long createAddressMapping(String jibunAddress, String roadAddress) throws Exception {
        String bjdCode = getDongCode(jibunAddress);
        List<DanjiCode> danjiCodes = danjiRepository.selectAllByBjdCode(bjdCode);

        AddressUtil.AddressComponents components = AddressUtil.parseAddress(roadAddress);

        // TODO: 해당 레포지토리 메서드는 단일 레코드가 아닌 집합을 리턴하기 때문에 모두 고려해야 한다
        List<AptInfo> aptInfoList = aptRepository.selectAptInfoByRoadComponents(components.getRoadNm(), components.getRoadNmBonbun(), components.getRoadNmBubun());
        if (aptInfoList.isEmpty()) {
            throw new Exception("해당 도로명주소에 대한 아파트 정보가 존재하지 않습니다.");
        }
        AptInfo aptInfo = aptInfoList.get(0);
        Long danjiId = null;
        for (DanjiCode danjiCode : danjiCodes) {
            danjiId = (aptInfo.getAptSeq() + danjiCode.getKaptCode()).hashCode() & 0xffffffffL;
            if (!AddressUtil.cleanAddress(jibunAddress).startsWith(AddressUtil.cleanAddress(danjiCode.getSiGugunDong())))
                continue;
            AddressUtil.OldAddressComponents oldAddressComponents = AddressUtil.parseOldAddress(jibunAddress);
            addressRepository.insertAddressMapping(
                    AddressMapping.builder()
                            .roadAddress(AddressUtil.cleanAddress(roadAddress))
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
}
