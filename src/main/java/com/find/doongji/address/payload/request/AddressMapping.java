package com.find.doongji.address.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressMapping {
    private Long id;
    private String oldAddress;
    private String roadAddress;
    private String umdNm;
    private String jibun;
    private String aptSeq;
    private int danjiId;
    private String bjdCode;
    private String aptNm;

    /**
     * 아래 필드를 합해서 도로명 주소를 구성할 수 있다.
     * 단, 부번은 없는 경우도 존재한다 (값은 0)
     */
    private String roadNm;
    private String roadNmBonbun;
    private String roadNmBubun;

}
