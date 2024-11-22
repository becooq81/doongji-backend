package com.find.doongji.danji.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DanjiCode {
    private String siGugunDong;
    private String bjdCode;
    private String kaptCode;
    private String kaptName;
    private String as1;
    private String as2;
    private String as3;

    @Override
    public String toString() {
        return "DanjiCode{" +
                "siGugunDong='" + siGugunDong + '\'' +
                ", bjdCode='" + bjdCode + '\'' +
                ", kaptCode='" + kaptCode + '\'' +
                ", kaptName='" + kaptName + '\'' +
                ", as1='" + as1 + '\'' +
                ", as2='" + as2 + '\'' +
                ", as3='" + as3 + '\'' +
                '}';
    }
}
