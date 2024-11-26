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

    private String bjdCode;
    private String kaptCode;
    private String kaptName;

}
