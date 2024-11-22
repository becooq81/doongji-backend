package com.find.doongji.apt.client;

import com.find.doongji.danji.payload.response.DanjiCode;
import com.find.doongji.search.payload.response.SearchResult;

import java.util.List;
import java.util.Map;

public interface AptClient {


    SearchResult getAptDetail(String kaptCode) throws Exception;

    Map<String, String> getCoordinatesFromDoroJuso(String doroJuso) throws Exception;
}
