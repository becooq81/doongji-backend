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
     * address mapping 레코드 한 번에 여러 개씩 추가
     * @param mappings
     */
    void bulkInsertAddressMapping(@Param("list") List<AddressMapping> mappings);

    /**
     * @param addressMapping
     * @return 맵핑 테이블에 주소 맵핑 추가 (맵핑 테이블에 존재하지 않는 주소만 추가)
     */
    void insertAddressMapping(AddressMapping addressMapping);

    /**
     * @param roadAddress 도로명주소
     * @return
     */
    List<AddressMappingResponse> selectAddressMappingByRoadAddress(@Param("roadAddress") String roadAddress);

    /**
     * @param jibunAddress
     * @return
     */
    List<AddressMappingResponse> selectAddressMappingByJibunAddress(@Param("jibunAddress") String jibunAddress);
    /**
     * @param danjiId 단지 ID
     * @return
     */
    AddressMappingResponse selectAddressMappingByDanjiId(@Param("danjiId") Long danjiId);

    /**
     * @param danjiIdList 단지 ID 리스트
     * @return
     */
    List<AddressMappingResponse> selectAddressMappingByDanjiIdList(@Param("list") List<Long> danjiIdList);

    /**
     * @param aptSeq
     */
    List<AddressMappingResponse> selectAddressMappingByAptSeq(@Param("aptSeq") String aptSeq);

}
