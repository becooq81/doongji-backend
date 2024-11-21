package com.find.doongji.apt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;

import com.find.doongji.apt.payload.response.AptDeal;
import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.apt.repository.AptRepository;
import com.find.doongji.location.payload.response.DongCode;
import com.find.doongji.location.service.LocationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicAptService implements AptService {

	private final AptRepository repo;
	private final LocationService service;
	
	@Override
	public List<AptDeal> findAptDealByAptNm(String aptName) throws NotFoundException {
		List<AptDeal> aptDeals = repo.selectAptDealByAptNm(aptName);
		if (aptDeals.isEmpty()) {
			throw new NotFoundException("Apt deal list is empty for apt name: "+aptName);
		}
		return aptDeals;
	}

	@Override
	public AptInfo findAptInfoByAptSeq(String aptSeq) throws NotFoundException {
		
		AptInfo aptInfo = repo.selectAptInfoByAptSeq(aptSeq);
		if (aptInfo == null) {
			throw new NotFoundException("Apt Info not found for apt seq: "+aptSeq);
		}
		return aptInfo;

	}

	@Override
	public List<AptDeal> findAptDealByDong(String sido, String gugun, String dong) throws NotFoundException {

		DongCode dongcode = service.findDongCode(sido, gugun, dong);
		if (dongcode.getDongcode() == null) {
	        throw new NotFoundException("DongCode not found for sido: " + sido + ", gugun: " + gugun + ", dong: " + dong);
		}
		
		return findAptDealByDongCode(dongcode.getDongcode());
	}

	@Override
	public List<AptDeal> findAptDealByDongCode(String dongCode) {
		Map<String, String> dongCodes = validateAndParseDongCode(dongCode);
		return repo.selectAptDealByDong(dongCodes.get("sggCd"), dongCodes.get("umdCd"));
	}

	@Override
	public List<AptInfo> findAptInfoByDongCode(String dongCode) {
		Map<String, String> dongCodes = validateAndParseDongCode(dongCode);
		return repo.selectAptInfoByDong(dongCodes.get("sggCd"), dongCodes.get("umdCd"));
	}

	private Map<String, String> validateAndParseDongCode(String dongCode) {
		if (dongCode == null || dongCode.isEmpty() || dongCode.length() != 10) {
			throw new IllegalArgumentException("Invalid dong code: " + dongCode);
		}

		String sggCd = dongCode.substring(0, 5);
		String umdCd = dongCode.substring(5);

		Map<String, String> result = new HashMap<>();
		result.put("sggCd", sggCd);
		result.put("umdCd", umdCd);

		return result;
	}
}
