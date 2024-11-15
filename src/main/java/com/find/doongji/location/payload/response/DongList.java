package com.find.doongji.location.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DongList {
	private String si;
	private String gugun;
	private List<String> dongList;
}
