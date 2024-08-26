package com.example.techlog.event.image;

import com.example.techlog.image.domain.Image;
import com.example.techlog.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ImageSaveEventHandler {

    private final ImageRepository imageRepository;

    @Async
    @EventListener(ImageSaveEvent.class)
    public void saveRefreshToken(ImageSaveEvent event) {
        for (String url : event.values()) {
            imageRepository.save(new Image(url, event.postId()));
        }
    }
}
