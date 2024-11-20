package com.find.doongji.listing.client;

import com.find.doongji.listing.payload.response.ClassificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ClassificationClient {

    private final RestTemplate restTemplate;

    @Value("${class.ai-url}")
    private String CLASSIFICATION_URL;

    public int classify(MultipartFile file) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(convertMultipartFileToFile(file)));
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<ClassificationResponse> response = restTemplate.postForEntity(CLASSIFICATION_URL, requestEntity, ClassificationResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                ClassificationResponse classificationResponse = response.getBody();
                if (classificationResponse != null) {
                    return classificationResponse.getResult();  // Assuming you need to return the 'result' field from the response
                } else {
                    throw new RuntimeException("API response body is null");
                }
            } else {
                throw new RuntimeException("Failed to classify file: " + response.getStatusCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to convert file: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to classify file: " + e.getMessage(), e);
        }


    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
