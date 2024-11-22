package com.find.doongji.danji.repository;

import com.find.doongji.danji.payload.request.DanjiEntity;
import com.find.doongji.danji.payload.response.DanjiCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DanjiRepository {

    /**
     * @return 테이블 존재 여부 반환
     */
    int checkIfTableExists();

    /**
     * 단지 코드 레코드 한 번에 여러 개씩 추가
     * @param danjis
     */
    void bulkInsertDanji(@Param("list") List<DanjiEntity> danjis);

    /**
     * 법정동 코드로 단지 코드 조회
     * @param bjdCode
     * @return
     */
    List<DanjiCode> selectAllByBjdCode(@Param("bjdCode") String bjdCode);

    /**
     * 아파트 명으로 단지 코드 조회
     * @param aptNm
     * @return
     */
    DanjiCode selectByAptNm(@Param("aptNm") String aptNm);
}
