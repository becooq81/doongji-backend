package com.find.doongji.address.repository;

import com.find.doongji.address.payload.request.AddressMapping;
import com.find.doongji.address.payload.response.AddressMappingResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressRepository {

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
    List<AddressMappingResponse> selectAddressMappingByDoroJuso(@Param("roadNm") String roadNm, @Param("roadNmBonbun") String roadNmBonbun, @Param("roadNmBubun") String roadNmBubun);

    /**
     * @param oldAddress 지번주소
     * @return
     */
    List<AddressMappingResponse> selectAddressMappingByOldAddress(@Param("oldAddress") String oldAddress);

    /**
     * @param roadAddress 도로명주소
     * @return
     */
    List<AddressMappingResponse> selectAddressMappingByRoadAddress(@Param("roadAddress") String roadAddress);
}
