package com.find.doongji.apt.repository;

import java.util.List;
import java.util.Map;

import com.find.doongji.apt.payload.request.AddressMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.find.doongji.apt.payload.response.AptDeal;
import com.find.doongji.apt.payload.response.AptInfo;

@Mapper
public interface AptRepository {

	/**
	 * @param aptSeq
	 * @return aptSeq에 해당하는 아파트 거래 내역 반환
	 */
	List<AptDeal> selectAptDealByAptSeq(String aptSeq);
	
	
	/**
	 * @param sggCd (시군구 코드), umdCd (읍면동 코드)
	 * @return 시군구, 읍면동 코드에 해당하는 아파트 거래 내역 반환
	 */
	List<AptDeal> selectAptDealByDong(@Param("sggCd") String sggCd, @Param("umdCd") String umdCd);

	/**
	 *  @param aptNm (아파트 명): 부분 검색 가능
	 *  @return 아파트 명에 해당하는 아파트 번호 반환
	 */
	List<String> selectAptSeqByAptNm(String aptNm);
	
	/**
	 * @param aptSeq
	 * @return 아파트 번호에 해당하는 아파트 정보 반환
	 */
	AptInfo selectAptInfoByAptSeq(String aptSeq);
	
	/**
	 * @param aptNm
	 * @return 아파트명에 해당하는 아파트 거래 내역 반환

	 */
	List<AptDeal> selectAptDealByAptNm(String aptNm);

	/**
	 * @param umdNm
	 * @param jibun
	 * @return umdNm(읍면동명)과 jibun(지번)에 해당하는 아파트 정보 반환
	 */
	List<AptInfo> selectAptInfoByUmdNmAndJibun(@Param("umdNm") String umdNm, @Param("jibun") String jibun);

	/**
	 * @return 맵핑 테이블 존재 여부 반환
	 */
	int checkIfMappingTableExists();

	/**
	 * @return 맵핑 테이블 생성
	 */
	void bulkInsertAddressMapping(@Param("list") List<AddressMapping> mappings);

	/**
	 * @param danjiId
	 * @return danjiId에 해당하는 법정동 코드 반환
	 */
	String selectBjdCodeByDanjiId(int danjiId);

	/**
	 * @param addressMapping
	 * @return 맵핑 테이블에 주소 맵핑 추가 (맵핑 테이블에 존재하지 않는 주소만 추가)
	 */
	void insertAddressMapping(AddressMapping addressMapping);

	/**
	 * @param roadNm
	 * @param roadNmBonbun
	 * @param roadNmBubun 도로명주소 숫자 부분에 다시가 없으면 0으로 대체
	 */
	List<AddressMapping> selectAddressMappingByDoroJuso(@Param("roadNm") String roadNm, @Param("roadNmBonbun") String roadNmBonbun, @Param("roadNmBubun") String roadNmBubun);
}
