package com.find.doongji.danji.repository;

import com.find.doongji.danji.payload.request.DanjiEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DanjiRepository {

    /**
     * @return 테이블 존재 여부 반환
     */
    int checkIfTableExists();

    void bulkInsertDanji(@Param("list") List<DanjiEntity> danjis);

}
