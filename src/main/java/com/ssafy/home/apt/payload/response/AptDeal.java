package com.ssafy.home.apt.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AptDeal {
	private int no;
	private String aptSeq;
	private String aptDong;
	private String floor;
	private int dealYear;
	private int dealMonth;
	private int dealDay;
	private double excluUseAr;
	private String dealAmount;
	private String address;
	private String aptNm;
	private String latitude;
	private String longitude;
}
