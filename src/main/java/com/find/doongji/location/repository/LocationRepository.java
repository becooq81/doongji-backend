package com.find.doongji.location.repository;

import com.find.doongji.location.payload.response.DongCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LocationRepository {

    // 시, 군구 값으로 모든 동을 찾는다
    List<String> selectDong(String si, String gugun);

    // 시 값으로 모든 구군을 찾는다
    List<String> selectGugun(String si);

    // 시, 군구, 동 값으로 일치하는 동코드를 찾는다
    String selectDongCode(String si, String gugun, String dong);

    // 모든 시도 값을 찾는다
    List<String> selectSido();

    // 법정동 코드로 시도, 구군, 동 값 조회
    DongCode selectDongCodeByDongcode(String dongcode);

    // 모든 동 코드를 찾는다
    List<String> selectAllDongCode();
}
