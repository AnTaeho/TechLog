package com.example.techlog.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.example.techlog.s3.dto.UploadResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3ImageService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public UploadResponse upload(MultipartFile file, String directoryName) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("올바른 파일 이름 형식이 아닙니다.");
        }

        validateExtension(fileName);

        String createFileName = createFileName(fileName, directoryName);
        ObjectMetadata objMeta = new ObjectMetadata();
        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        objMeta.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

        amazonS3Client.putObject(
                new PutObjectRequest(bucket, createFileName, byteArrayIs, objMeta)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        return new UploadResponse(
                amazonS3Client.getUrl(bucket, createFileName).toString()
        );
    }

    public void delete(String fileUrl) {
        String objectKey = fileUrl.split(bucket + ".s3." + region + ".amazonaws.com/")[1];
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, objectKey));
    }

    private String createFileName(String fileName, String directoryName) {
        return directoryName + "/" + UUID.randomUUID() + fileName;
    }

    private void validateExtension(String fileName) {
        if (!FileExtension.isValidExtension(fileName)) {
            throw new IllegalArgumentException("올바른 파일 확장자 형식이 아닙니다. (jpg, jpeg, png)");
        }
    }
}
