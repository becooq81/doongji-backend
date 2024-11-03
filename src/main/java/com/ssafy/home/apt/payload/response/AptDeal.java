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
	
	@Override
	public String toString() {
		return "AptDeal [no=" + no + ", aptSeq=" + aptSeq + ", aptDong=" + aptDong + ", floor=" + floor + ", dealYear="
				+ dealYear + ", dealMonth=" + dealMonth + ", dealDay=" + dealDay + ", excluUseAr=" + excluUseAr
				+ ", dealAmount=" + dealAmount + ", address=" + address + ", aptNm=" + aptNm + ", latitude=" + latitude
				+ ", longitude=" + longitude + "]";
	}
	
	
}
