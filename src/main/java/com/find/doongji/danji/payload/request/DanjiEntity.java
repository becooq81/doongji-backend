package com.find.doongji.danji.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanjiEntity {

    private String as1;
    private String as2;
    private String as3;
    private String bjdCode;
    private String kaptCode;
    private String kaptName;

}
