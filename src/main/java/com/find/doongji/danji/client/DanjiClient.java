package com.find.doongji.danji.client;

import com.find.doongji.apt.util.HttpUtils;
import com.find.doongji.apt.util.ParseUtils;
import com.find.doongji.danji.payload.request.DanjiEntity;
import com.find.doongji.danji.repository.DanjiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DanjiClient {

    private final DanjiRepository danjiRepository;

    @Value("${apt.danji-code-url}")
    private String DANJI_CODE_URL;

    @Value("${apt.public-data-key}")
    private String PUBLIC_DATA_KEY;

    public List<DanjiEntity> fetchItemsForBjdCodes(List<String> bjdCodes) throws Exception {
        List<DanjiEntity> allEntities = new ArrayList<>();

        String requestUrl = DANJI_CODE_URL +
                "?ServiceKey=" + PUBLIC_DATA_KEY;

        for (String bjdCode : bjdCodes) {
            int pageNo = 1;
            boolean hasMorePages;

            do {
                String fullUrl = requestUrl + "&bjdCode=" + bjdCode + "&pageNo=" + pageNo + "&numOfRows=10";
                System.out.println("Url: " + fullUrl);

                String responseBody = HttpUtils.fetchDataFromUrl(fullUrl);
                System.out.println("Response: " + responseBody);
                List<Map<String, String>> result = ParseUtils.parseXML(
                        "/response/body/items/item",
                        responseBody,
                        "as1", "as2", "as3", "bjdCode", "kaptCode", "kaptName"
                );

                List<DanjiEntity> entities = result.stream()
                        .map(map -> DanjiEntity.builder()
                                .bjdCode(map.get("bjdCode"))
                                .kaptCode(map.get("kaptCode"))
                                .kaptName(map.get("kaptName"))
                                .build()
                        )
                        .toList();
                allEntities.addAll(entities);

                if (allEntities.size() >= 100) {
                    danjiRepository.bulkInsertDanji(allEntities);
                    allEntities.clear();
                }

                int totalCount = Integer.parseInt(ParseUtils.parseXML(
                        "/response/body",
                        responseBody,
                        "totalCount"
                ).get(0).get("totalCount"));

                int numOfRows = Integer.parseInt(ParseUtils.parseXML(
                        "/response/body",
                        responseBody,
                        "numOfRows"
                ).get(0).get("numOfRows"));

                int totalPages = (int) Math.ceil((double) totalCount / numOfRows);
                hasMorePages = pageNo < totalPages;

                pageNo++;
            } while (hasMorePages);
        }

        return allEntities;
    }
}
