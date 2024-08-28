package com.example.techlog.event.image;

import com.example.techlog.image.domain.Image;
import com.example.techlog.image.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ImageSaveEventHandlerTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageSaveEventHandler imageSaveEventHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveRefreshToken_shouldSaveImagesToRepository() {
        // given
        Long postId = 1L;
        List<String> imageUrls = List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg");
        ImageSaveEvent imageSaveEvent = new ImageSaveEvent(imageUrls, postId);

        // when
        imageSaveEventHandler.saveRefreshToken(imageSaveEvent);

        // then
        ArgumentCaptor<Image> captor = ArgumentCaptor.forClass(Image.class);
        verify(imageRepository, times(2)).save(captor.capture());
        List<Image> savedImages = captor.getAllValues();

        assertEquals(2, savedImages.size());
        assertEquals(imageUrls.get(0), savedImages.get(0).getImageUrl());
        assertEquals(postId, savedImages.get(0).getPostId());
        assertEquals(imageUrls.get(1), savedImages.get(1).getImageUrl());
        assertEquals(postId, savedImages.get(1).getPostId());
    }
}
