package com.example.blog.service;

import com.example.blog.dto.CommentDto;
import com.example.blog.model.Comment;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public Comment createComment(Long postId, CommentDto dto, Authentication authentication) {
        Post post = postRepository.findById(postId).orElseThrow();
        User author = (User) userService.loadUserByUsername(authentication.getName());
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setPost(post);
        comment.setAuthor(author);
        if (dto.getParentCommentId() != null) {
            Comment parent = commentRepository.findById(dto.getParentCommentId()).orElseThrow();
            comment.setParentComment(parent);
        }
        return commentRepository.save(comment);
    }

    // Métodos similares para update/delete, com checagem de autorização (autor ou admin)

    public Page<Comment> getCommentsByPost(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId).orElseThrow();
        return commentRepository.findByPost(post, pageable);
    }
}