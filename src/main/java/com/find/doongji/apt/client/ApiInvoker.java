package com.find.doongji.apt.client;

import com.find.doongji.apt.payload.response.DanjiCode;
import com.find.doongji.apt.payload.response.SearchResult;

import java.util.List;

public interface ApiInvoker {

    List<DanjiCode> getDanjiCodeList(String bjdCode) throws Exception;

    SearchResult getAptDetail(String kaptCode) throws Exception;
}
