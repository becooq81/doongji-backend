package com.find.doongji.listing.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUploadUtil {

    public static String uploadFile(MultipartFile file, String uploadDir) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("Invalid file name");
        }

        String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        String uniqueFileName = System.currentTimeMillis() + "_" + sanitizedFileName;

        String filePath = uploadDir + "/" + uniqueFileName; // Use '/' for web paths

        File destFile = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(destFile)) {
            fos.write(file.getBytes());
            System.out.println("File successfully uploaded: " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }

        return filePath;

    }
}
