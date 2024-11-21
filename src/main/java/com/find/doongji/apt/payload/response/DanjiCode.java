package com.find.doongji.apt.payload.response;

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

    @Override
    public String toString() {
        return "DanjiCode{" +
                "roadAddress='" + siGugunDong + '\'' +
                ", bjdCode='" + bjdCode + '\'' +
                ", kaptCode='" + kaptCode + '\'' +
                ", kaptName='" + kaptName + '\'' +
                '}';
    }
}
