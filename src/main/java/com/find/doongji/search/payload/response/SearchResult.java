package com.find.doongji.search.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SearchResult  {

    private String kaptName; // 단지 이름
    private String hoCnt; // 호 수
    private String kaptCode; // 단지 코드
    private String kaptDongCnt; // 동 수
    private String kaptAddr; // 법정동 주소 full ver
    private String kaptdaCnt; // 세대 수
    private String doroJuso; // 도로명 주소

    // 상세 정보 API
    /**
     * Comma-separated list of convenient facilities
     * Example: "편의점,주차장,놀이터"
     */
    private String convenientFacility;

    /**
     * Comma-separated list of educational facilities
     * Example: "초등학교,중학교"
     */
    private String educationFacility;

    /**
     * Bus station walking time in minutes
     * Format: "n분"
     */
    private String busWalkingTime;

    /**
     * Subway station walking time in minutes
     * Format: "n분"
     */
    private String subwayWalkingTime;

    /**
     * Subway line number or name
     * Example: "2호선", "분당선"
     */
    private String subwayLine;

    /**
     * Name of the nearest subway station
     * Example: "강남역"
     */
    private String subwayStation;

    private String longitude;
    private String latitude;
}
