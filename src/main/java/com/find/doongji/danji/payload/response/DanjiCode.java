package com.find.doongji.danji.payload.response;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DanjiCode {
    @Setter
    private String sidoGugunDong;
    private String bjdCode;
    private String kaptCode;
    private String kaptName;
}
