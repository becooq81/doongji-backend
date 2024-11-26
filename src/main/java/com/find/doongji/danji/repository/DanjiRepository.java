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
     * 아파트 명으로 단지 코드 조회
     * @param aptNm
     * @return
     */
    List<DanjiCode> selectByAptNmAndDongcode(@Param("aptNm") String aptNm, @Param("dongcode") String dongcode);
}
