package com.corkboard.backend;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(originPatterns = "*")
public class PostController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LogService logService;

    public PostController(PostRepository postRepository, CommentRepository commentRepository, LogService logService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.logService = logService;
    }

    @GetMapping
    public List<Post> getAllPosts(HttpServletRequest request) {
        logGetRequest(request);
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id, HttpServletRequest request) {
        logGetRequest(request);
        return postRepository.findById(id).orElse(null);
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getCommentsByPostId(@PathVariable Long id, HttpServletRequest request) {
        logGetRequest(request);
        return commentRepository.findByPostId(id);
    }

    @PostMapping
    public Post createPost(@RequestParam("title") String title,
                           @RequestParam("content") String content,
                           @RequestParam("username") String username,
                           @RequestParam(value = "file", required = false) MultipartFile file,
                           HttpServletRequest request) throws IOException {
        logPostRequest(request, username);
        String filePath;
        if (file != null && !file.isEmpty()) {
            transferFile(file);
            filePath = "uploads/" + file.getOriginalFilename();
        } else {
            filePath = null;
        }

        Post post = new Post(title, content, username, filePath, LocalDateTime.now());
        return postRepository.save(post);
    }

    @Scheduled(fixedRate = 300000)
    public void deleteExpiredPosts() {
        List<Post> posts = postRepository.findByCreatedAtBefore(LocalDateTime.now().minusDays(1));
        postRepository.deleteAll(posts);
    }

    public void logPostRequest(HttpServletRequest request, String username) {
        logService.logInfo("POST from " + request.getRemoteAddr() + " at " + LocalDateTime.now() + " by " + username);
    }

    public void transferFile(MultipartFile file) throws IOException {
        file.transferTo(new java.io.File("uploads/" + file.getOriginalFilename()));
    }

    public void logGetRequest(HttpServletRequest request) {
        logService.logInfo("GET from " + request.getRemoteAddr() + " at " + LocalDateTime.now());
    }
}