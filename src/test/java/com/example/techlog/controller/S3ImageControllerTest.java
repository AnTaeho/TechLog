package com.example.techlog.controller;

import com.example.techlog.common.dto.CommonResponse;
import com.example.techlog.common.dto.EmptyDto;
import com.example.techlog.s3.S3ImageController;
import com.example.techlog.s3.S3ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class S3ImageControllerTest {

    @Mock
    private S3ImageService s3ImageService;

    @InjectMocks
    private S3ImageController s3ImageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteImage() {
        // given
        String fileUrl = "https://example.com/test.jpg";

        // when
        CommonResponse<EmptyDto> response = s3ImageController.deleteImage(fileUrl);

        // then
        assertEquals(CommonResponse.EMPTY, response);
        verify(s3ImageService, times(1)).delete(anyString());
    }
}
