package com.find.doongji.apt.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressMapping {
    private String oldAddress;
    private String umdNm;
    private String jibun;
    private String aptSeq;
    private int danjiId;
    private String bjdCode;
}
