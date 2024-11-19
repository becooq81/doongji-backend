package com.find.doongji.apt.client;

import com.find.doongji.apt.payload.response.DanjiCode;
import com.find.doongji.apt.payload.response.SearchResult;
import com.find.doongji.apt.utils.HttpUtils;
import com.find.doongji.apt.utils.ParseUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AptDetailClient implements AptClient {

    @Value("${apt.danji-code-url}")
    private String DANJI_CODE_URL;

    @Value("${apt.basic-info-url}")
    private String BASIC_INFO_URL;

    @Value("${apt.specific-info-url}")
    private String SPECIFIC_INFO_URL;

    @Value("${apt.geocoder-url}")
    private String GEOCODER_URL;

    @Value("${apt.public-data-key}")
    private String PUBLIC_DATA_KEY;

    @Value("${GEOCODER_SERVICE_KEY}")
    private String GEOCODER_KEY;


    @Override
    public List<DanjiCode> getDanjiCodeList(String bjdCode) throws Exception {
        StringBuilder dataUrlBuilder = new StringBuilder(DANJI_CODE_URL)
                .append("?ServiceKey=").append(PUBLIC_DATA_KEY)
                .append("&bjdCode=").append(bjdCode);

        String responseBody = HttpUtils.fetchDataFromUrl(dataUrlBuilder.toString());

        List<Map<String, String>> result = ParseUtils.parseXML("/response/body/items/item", responseBody, "kaptCode", "kaptName");

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


        Map<String, String> basicInfoResult = ParseUtils.parseXML("/response/body/item", basicInfoResponseBody, "kaptName", "hoCnt", "kaptCode", "kaptDongCnt", "doroJuso", "kaptdaCnt")
                .get(0);
        Map<String, String> specificInfoResult = ParseUtils.parseXML("/response/body/item", specificInfoResponseBody, "convenientFacility", "educationFacility", "kaptdWtimebus", "kaptdWtimesub", "subwayLine", "subwayStation")
                .get(0);

        String address = basicInfoResult.get("doroJuso");
        if (address.equals("")) {
            return null;
        }
        StringBuilder geocoderUrlBuilder = new StringBuilder(GEOCODER_URL)
                .append(GEOCODER_KEY)
                .append("&address=").append(URLEncoder.encode(address, StandardCharsets.UTF_8));

        String geocoderResponseBody = HttpUtils.fetchDataFromUrl(geocoderUrlBuilder.toString());

        Map<String, String> geocoderResult = ParseUtils.parseXML("/response/result/point", geocoderResponseBody, "x", "y").get(0);

        return new SearchResult(
                basicInfoResult.get("kaptName"),
                basicInfoResult.get("hoCnt"),
                basicInfoResult.get("kaptCode"),
                basicInfoResult.get("kaptDongCnt"),
                basicInfoResult.get("doroJuso"),
                basicInfoResult.get("kaptdaCnt"),
                specificInfoResult.get("convenientFacility"),
                specificInfoResult.get("educationFacility"),
                specificInfoResult.get("kaptdWtimebus"),
                specificInfoResult.get("kaptdWtimesub"),
                specificInfoResult.get("subwayLine"),
                specificInfoResult.get("subwayStation"),
                geocoderResult.get("x"),
                geocoderResult.get("y")
        );
    }
}
