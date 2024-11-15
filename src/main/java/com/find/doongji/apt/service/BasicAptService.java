package com.find.doongji.apt.service;

import java.util.List;

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
		String sggCd = dongcode.getDongcode().substring(0, 5);
		String umdCd = dongcode.getDongcode().substring(5);
		
		List<AptDeal> aptDeals = repo.selectAptDealByDong(sggCd, umdCd);
		return aptDeals;
	}

}
