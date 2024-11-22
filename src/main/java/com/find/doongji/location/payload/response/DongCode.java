package com.find.doongji.location.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DongCode {
    private String sidoName;
    private String gugunName;
    private String dongName;
    private String dongCode;

    @Override
    public String toString() {
        return "DongCode{" +
                "sidoName='" + sidoName + '\'' +
                ", gugunName='" + gugunName + '\'' +
                ", dongName='" + dongName + '\'' +
                ", dongCode='" + dongCode + '\'' +
                '}';
    }
}
