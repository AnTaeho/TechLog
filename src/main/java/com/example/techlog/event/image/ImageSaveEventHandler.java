package com.example.techlog.event.image;

import com.example.techlog.image.domain.Image;
import com.example.techlog.image.domain.ImageOutboxEvent;
import com.example.techlog.image.repository.ImageOutboxEventRepository;
import com.example.techlog.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ImageSaveEventHandler {

    private final ImageRepository imageRepository;
    private final ImageOutboxEventRepository imageOutboxEventRepository;

    @TransactionalEventListener(value = ImageSaveEvent.class, phase = TransactionPhase.BEFORE_COMMIT)
    public void savePublishRecord(ImageSaveEvent event) {
        Long postId = event.postId();
        for (String url : event.values()) {
            imageOutboxEventRepository.save(new ImageOutboxEvent(url, postId));
        }
    }

    @Async
    @TransactionalEventListener(value = ImageSaveEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void executePublishedEvent(ImageSaveEvent event) {
        Long postId = event.postId();
        for (String url : event.values()) {
            try {
                imageRepository.save(new Image(url, postId));
                Thread.sleep(5000);
                imageOutboxEventRepository.updateExecuted(url, postId);
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
    }
}
