package com.find.doongji.apt.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult  {

    private int hoCnt; // 호 수
    private String kaptCode; // 단지 코드
    private int kaptDongCnt; // 동 수
    private String kaptAddr; // 법정동 주소 full ver
    private int kaptdaCnt; // 세대 수

    // 상세 정보 API
    private String convenientFacility;
    private String educationFacility;
    private String kaptdWtimebus;
    private String kaptdWtimesub;
    private String subwayLine;
    private String subwayStation;

    private double longitude;
    private double latitude;
}
