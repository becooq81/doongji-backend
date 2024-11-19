package com.find.doongji.search.client;

import com.find.doongji.search.payload.response.RecommendResponse;
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
public class RecommendClient {


    @Value("${rec.ai-url}")
    private String AI_URL;

    public List<RecommendResponse> getRecommendation(String query, int topK) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = String.format("{\"query\": \"%s\", \"top_k\": %d}", query, topK);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(AI_URL, requestEntity, String.class);

        try {
            return parseJson(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

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


}
