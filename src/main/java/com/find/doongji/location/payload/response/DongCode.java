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
	private String sido;
	private String gugun;
	private String dong;
	private String dongcode;
}
