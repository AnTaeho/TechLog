package com.example.techlog.s3.dto;

import org.springframework.web.multipart.MultipartFile;

public record UploadRequest(
        MultipartFile files,
        String directoryName
) {
}
