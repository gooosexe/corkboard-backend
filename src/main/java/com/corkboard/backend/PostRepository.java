package com.corkboard.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCreatedAtBefore(LocalDateTime time);
    void deleteByCreatedAtBefore(LocalDateTime time);
}
