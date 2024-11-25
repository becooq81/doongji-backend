package com.find.doongji.apt.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AptInfo {
    private String aptSeq;
    private String sggCd;
    private String umdCd;
    private String umdNm;
    private String jibun;
    private String roadNmSggCd;
    private String roadNm;
    private String roadNmBonbun;
    private String roadNmBubun;
    private String aptNm;
    private int buildYear;
    private String latitude;
    private String longitude;

    private String minExcluUseAr;
    private String maxExcluUseAr;
    private String minDealAmount;
    private String maxDealAmount;

    @Override
    public String toString() {
        return "AptInfo{" +
                "aptSeq='" + aptSeq + '\'' +
                ", sggCd='" + sggCd + '\'' +
                ", umdCd='" + umdCd + '\'' +
                ", umdNm='" + umdNm + '\'' +
                ", jibun='" + jibun + '\'' +
                ", roadNmSggCd='" + roadNmSggCd + '\'' +
                ", roadNm='" + roadNm + '\'' +
                ", roadNmBonbun='" + roadNmBonbun + '\'' +
                ", roadNmBubun='" + roadNmBubun + '\'' +
                ", aptNm='" + aptNm + '\'' +
                '}';
    }
}
