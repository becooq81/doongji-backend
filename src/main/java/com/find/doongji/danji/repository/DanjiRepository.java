package com.find.doongji.danji.payload.response;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DanjiRepository {

    /**
     * @return 테이블 존재 여부 반환
     */
    int checkIfTableExists();

    void bulkInsertDanji(@Param("list") List<DanjiEntity> danjis);

}
