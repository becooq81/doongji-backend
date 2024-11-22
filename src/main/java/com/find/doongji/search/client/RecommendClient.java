package com.find.doongji.search.client;

import com.find.doongji.search.payload.response.RecommendResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RecommendClient {

    private final RestTemplate restTemplate;

    @Value("${rec.ai-url}")
    private String RECOMMEND_URL;

    private static List<RecommendResponse> parseJson(String responseBody) throws Exception {
        List<RecommendResponse> result = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(responseBody);

            JSONArray resultDataArray = jsonResponse.getJSONArray("results");

            for (int i = 0; i < resultDataArray.length(); i++) {
                JSONObject item = resultDataArray.getJSONObject(i);

                int danjiId = item.getInt("danji_id");
                float similarity = (float) item.getDouble("similarity");

                result.add(new RecommendResponse(danjiId, similarity));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing JSON", e);
        }

        return result;
    }

    public List<RecommendResponse> getRecommendation(String query, int topK) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("query", query);
        requestBody.put("top_k", topK);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(RECOMMEND_URL, requestEntity, String.class);
        try {
            return parseJson(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}
