package com.corkboard.backend;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostDeletionService {

    private final PostRepository postRepository;

    public PostDeletionService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void deleteExpiredPosts() {
        postRepository.deleteByCreatedAtBefore(LocalDateTime.now().minusDays(1));
    }
}