package com.find.doongji.listing.client;

import com.find.doongji.listing.payload.response.ClassificationApiResponse;
import com.find.doongji.listing.payload.response.ClassificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ClassificationClient {

    private final RestTemplate restTemplate;

    @Value("${class.ai-url}")
    private String CLASSIFICATION_URL;

    public ClassificationResponse classify(MultipartFile file, String uploadDir) {

        try {

            String filePath = saveMultipartFileToDirectory(file, uploadDir);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(filePath));
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<ClassificationApiResponse> response = restTemplate.postForEntity(CLASSIFICATION_URL, requestEntity, ClassificationApiResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                ClassificationApiResponse apiResponse = response.getBody();
                if (apiResponse != null) {
                    return new ClassificationResponse(apiResponse.getResult(), filePath);
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

    private String saveMultipartFileToDirectory(MultipartFile file, String uploadDir) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = uploadDir + File.separator + uniqueFileName;

        File destFile = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(destFile)) {
            fos.write(file.getBytes());
        }
        return filePath;
    }
}
