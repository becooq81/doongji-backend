package com.find.doongji.danji.repository;

import com.find.doongji.danji.payload.request.DanjiEntity;
import com.find.doongji.danji.payload.response.DanjiCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface DanjiRepository {

    /**
     * @return 테이블 존재 여부 반환
     */
    int checkIfTableExists();

    void bulkInsertDanji(@Param("list") List<DanjiEntity> danjis);

    List<DanjiCode> selectAllByBjdCode(@Param("bjdCode") String bjdCode);

    List<DanjiCode> selectAllByBjdCodeList(@Param("list") Set<String> bjdCodeList);

    DanjiCode selectByAptNm(@Param("aptNm") String aptNm);
}
