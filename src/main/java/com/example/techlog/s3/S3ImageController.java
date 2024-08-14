package com.example.techlog.s3;

import com.example.techlog.common.dto.CommonResponse;
import com.example.techlog.common.dto.EmptyDto;
import com.example.techlog.s3.dto.UploadRequest;
import com.example.techlog.s3.dto.UploadResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class S3ImageController {

    private final S3ImageService s3ImageService;

    @PostMapping
    public CommonResponse<UploadResponse> uploadFile(@ModelAttribute UploadRequest uploadRequest) throws IOException {
        System.out.println("업로드 시작");
        return new CommonResponse<>(s3ImageService.upload(
                uploadRequest.files()
        ));
    }

    @DeleteMapping
    public CommonResponse<EmptyDto> deleteImage(@RequestParam("fileUrl") String fileUrl) {
        s3ImageService.delete(fileUrl);
        return CommonResponse.EMPTY;
    }
}
