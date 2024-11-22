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
        String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        String uniqueFileName = System.currentTimeMillis() + "_" + sanitizedFileName;

        String filePath = uploadDir + File.separator + uniqueFileName;

        File destFile = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(destFile)) {
            fos.write(file.getBytes());
        }
        return filePath;
    }
}
