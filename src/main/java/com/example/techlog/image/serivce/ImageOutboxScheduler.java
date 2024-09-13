package com.example.techlog.image.serivce;

import com.example.techlog.image.domain.ImageOutboxEvent;
import com.example.techlog.image.repository.ImageOutboxEventRepository;
import com.example.techlog.image.domain.Image;
import com.example.techlog.image.repository.ImageRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageOutboxScheduler {

    private final ImageRepository imageRepository;
    private final ImageOutboxEventRepository imageOutboxEventRepository;

    @Scheduled(fixedDelay = 300000)
    public void executeFailedEvent() {
        List<ImageOutboxEvent> result =imageOutboxEventRepository.findAllFailEvent();
        for (ImageOutboxEvent imageOutboxEvent : result) {
            imageRepository.save(new Image(imageOutboxEvent.getImageUrl(), imageOutboxEvent.getPostId()));
            imageOutboxEventRepository.updateExecuted(imageOutboxEvent.getImageUrl(), imageOutboxEvent.getPostId());
        }
    }

    @Scheduled(fixedDelay = 3600000)
    public void deleteOldEvent() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        imageOutboxEventRepository.deleteOlderThan(oneHourAgo);
    }
}
