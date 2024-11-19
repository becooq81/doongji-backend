package com.find.doongji.apt.client;

import com.find.doongji.apt.payload.response.DanjiCode;
import com.find.doongji.apt.payload.response.SearchResult;
import com.find.doongji.apt.utils.HttpUtils;
import com.find.doongji.apt.utils.ParseUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AptDetailInvoker implements ApiInvoker{

    @Value("${apt.danji-code-url}")
    private String DANJI_CODE_URL;

    @Value("${apt.basic-info-url}")
    private String BASIC_INFO_URL;

    @Value("${apt.specific-info-url")
    private String SPECIFIC_INFO_URL;

    @Value("${apt.geocoder-url}")
    private String GEOCODER_URL;

    @Value("${apt.public-data-key}")
    private String PUBLIC_DATA_KEY;

    @Value("${apt.geocoder-key}")
    private String GEOCODER_KEY;

    @Override
    public List<DanjiCode> getDanjiCodeList(String bjdCode) throws Exception {
        StringBuilder dataUrlBuilder = new StringBuilder(DANJI_CODE_URL)
                .append("?ServiceKey=").append(PUBLIC_DATA_KEY)
                .append("&bjdCode=").append(bjdCode);

        System.out.println(dataUrlBuilder.toString());

        String responseBody = HttpUtils.fetchDataFromUrl(dataUrlBuilder.toString());

        List<Map<String, String>> result = ParseUtils.parseXML(responseBody, "kaptCode", "kaptName");

        List<DanjiCode> danjiCodes = new ArrayList<>();
        for (Map<String, String> map : result) {
            danjiCodes.add(new DanjiCode(bjdCode, map.get("kaptCode"), map.get("kaptName")));
        }
        return danjiCodes;
    }

    @Override
    public SearchResult getAptDetail(String kaptCode) throws Exception {
        StringBuilder basicInfoUrlBuilder = new StringBuilder(BASIC_INFO_URL)
                .append("?ServiceKey=").append(PUBLIC_DATA_KEY)
                .append("&kaptCode=").append(kaptCode);
        StringBuilder specificInfoUrlBuilder = new StringBuilder(SPECIFIC_INFO_URL)
                .append("?ServiceKey=").append(PUBLIC_DATA_KEY)
                .append("&kaptCode=").append(kaptCode);

        String basicInfoResponseBody = HttpUtils.fetchDataFromUrl(basicInfoUrlBuilder.toString());
        String specificInfoResponseBody = HttpUtils.fetchDataFromUrl(specificInfoUrlBuilder.toString());

        Map<String, String> basicInfoResult = ParseUtils.parseXML(basicInfoResponseBody, "hoCnt", "kaptCode", "kaptDongCnt", "kaptAddr", "kaptdaCnt")
                .get(0);
        Map<String, String> specificInfoResult = ParseUtils.parseXML(specificInfoResponseBody, "convenientFacility", "educationFacility", "kaptdWtimebus", "kaptdWtimesub", "subwayLine", "subwayStation")
                .get(0);

        String address = basicInfoResult.get("kaptAddr");
        StringBuilder geocoderUrlBuilder = new StringBuilder(GEOCODER_URL)
                .append("?accessToken=").append(GEOCODER_KEY)
                .append("&address=").append(address);

        String geocoderResponseBody = HttpUtils.fetchDataFromUrl(geocoderUrlBuilder.toString());
        Map<String, String> geocoderResult = ParseUtils.parseJson(geocoderResponseBody, "X", "Y").get(0);

        return new SearchResult(
                Integer.parseInt(basicInfoResult.get("hoCnt")),
                basicInfoResult.get("kaptCode"),
                Integer.parseInt(basicInfoResult.get("kaptDongCnt")),
                basicInfoResult.get("kaptAddr"),
                Integer.parseInt(basicInfoResult.get("kaptdaCnt")),
                specificInfoResult.get("convenientFacility"),
                specificInfoResult.get("educationFacility"),
                specificInfoResult.get("kaptdWtimebus"),
                specificInfoResult.get("kaptdWtimesub"),
                specificInfoResult.get("subwayLine"),
                specificInfoResult.get("subwayStation"),
                Double.parseDouble(geocoderResult.get("X")),
                Double.parseDouble(geocoderResult.get("Y"))
        );
    }
}
