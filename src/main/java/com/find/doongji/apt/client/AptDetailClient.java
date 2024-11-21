package com.find.doongji.apt.client;

import com.find.doongji.address.util.RoadAddressUtil;
import com.find.doongji.apt.payload.response.DanjiCode;
import com.find.doongji.search.payload.response.SearchResult;
import com.find.doongji.apt.utils.HttpUtils;
import com.find.doongji.apt.utils.ParseUtils;
import com.find.doongji.search.util.ResultParser;
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
        List<Map<String, String>> result = ParseUtils.parseXML("/response/body/items/item", responseBody, "as1", "as2", "as3", "kaptCode", "kaptName");

        List<DanjiCode> danjiCodes = new ArrayList<>();
        for (Map<String, String> map : result) {
            danjiCodes.add(new DanjiCode(
                    RoadAddressUtil.cleanAddress(map.get("as1") + " " + map.get("as2") + " " + map.get("as3")),
                    bjdCode, map.get("kaptCode"), map.get("kaptName")));
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

        Map<String, String> basicInfoResult = ParseUtils.parseXML("/response/body/item", basicInfoResponseBody, "kaptName", "kaptAddr", "hoCnt", "kaptCode", "kaptDongCnt", "doroJuso", "kaptdaCnt")
                .get(0);
        Map<String, String> specificInfoResult = ParseUtils.parseXML("/response/body/item", specificInfoResponseBody, "convenientFacility", "educationFacility", "kaptdWtimebus", "kaptdWtimesub", "subwayLine", "subwayStation")
                .get(0);

        String address = basicInfoResult.get("kaptAddr");

        String latitude = null;
        String longitude = null;
        if (address != null && !address.isEmpty()) {
            Map<String, String> geocoderResult = getCoordinatesFromDoroJuso(address);
            latitude = geocoderResult.get("y");
            longitude = geocoderResult.get("x");
        }

        return SearchResult.builder()
                .kaptName(basicInfoResult.get("kaptName"))
                .hoCnt(basicInfoResult.get("hoCnt"))
                .kaptCode(basicInfoResult.get("kaptCode"))
                .kaptDongCnt(basicInfoResult.get("kaptDongCnt"))
                .kaptAddr(basicInfoResult.get("kaptAddr"))
                .kaptdaCnt(basicInfoResult.get("kaptdaCnt"))
                .doroJuso(basicInfoResult.get("doroJuso"))
                .convenientFacility(ResultParser.parseToDetails(specificInfoResult.get("convenientFacility")))
                .educationFacility(ResultParser.parseToDetails(specificInfoResult.get("educationFacility")))
                .busWalkingTime(specificInfoResult.get("kaptdWtimebus"))
                .subwayWalkingTime(specificInfoResult.get("kaptdWtimesub"))
                .subwayLine(specificInfoResult.get("subwayLine"))
                .subwayStation(specificInfoResult.get("subwayStation"))
                .latitude(latitude)
                .longitude(longitude)
                .build();

    }

    @Override
    public Map<String, String> getCoordinatesFromDoroJuso(String doroJuso) throws Exception {
        StringBuilder geocoderUrlBuilder = new StringBuilder(GEOCODER_URL)
                .append(GEOCODER_KEY)
                .append("&address=").append(URLEncoder.encode(doroJuso, StandardCharsets.UTF_8));
        String geocoderResponseBody = HttpUtils.fetchDataFromUrl(geocoderUrlBuilder.toString());

        return ParseUtils.parseXML("/response/result/point", geocoderResponseBody, "x", "y").get(0);
    }


}
