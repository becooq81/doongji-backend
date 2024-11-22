package com.find.doongji.apt.repository;

import com.find.doongji.apt.payload.response.AptDeal;
import com.find.doongji.apt.payload.response.AptInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

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
     * @param sggCd (시군구 코드), umdCd (읍면동 코드)
     */
    List<AptInfo> selectAptInfoByDong(@Param("sggCd") String sggCd, @Param("umdCd") String umdCd);

    /**
     * @param aptNm (아파트 명): 부분 검색 가능
     * @return 아파트 명에 해당하는 아파트 번호 반환
     */
    List<String> selectAptSeqByAptNm(String aptNm);

    /**
     * @param aptSeq
     * @return 아파트 번호에 해당하는 아파트 정보 반환
     */
    AptInfo selectAptInfoByAptSeq(String aptSeq);

    /**
     * apt seq 리스트로 아파트 정보 반환
     * @param aptSeqList
     * @return
     */
    List<AptInfo> selectAptInfoByAptSeqList(@Param("list") List<String> aptSeqList);

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
     * @param roadNm
     * @param roadNmBonbun
     * @param roadNmBubun
     * @return
     */
    List<AptInfo> selectAptInfoByRoadComponents(@Param("roadNm") String roadNm, @Param("roadNmBonbun") String roadNmBonbun, @Param("roadNmBubun") String roadNmBubun);


    /**
     * 모든 아파트 정보 반환
     * @return
     */
    List<AptInfo> findAllAptInfos();
}
