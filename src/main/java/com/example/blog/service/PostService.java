package com.example.blog.service;

import com.example.blog.dto.PostDto;
import com.example.blog.dto.PostResponse;
import com.example.blog.model.Post;
import com.example.blog.model.Tag;
import com.example.blog.model.User;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository, TagRepository tagRepository, UserService userService) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.userService = userService;
    }

    public PostResponse createPost(PostDto dto, Authentication authentication) {
        User author = (User) userService.loadUserByUsername(authentication.getName());
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAuthor(author);
        if (dto.getTags() != null) {
            for (String tagName : dto.getTags()) {
                Tag tag = tagRepository.findByName(tagName).orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setName(tagName);
                    return tagRepository.save(newTag);
                });
                post.getTags().add(tag);
            }
        }
        Post saved = postRepository.save(post);
        return mapToResponse(saved);
    }

    public PostResponse updatePost(Long id, PostDto dto, Authentication authentication) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post não encontrado"));
        User currentUser = (User) userService.loadUserByUsername(authentication.getName());
        if (!post.getAuthor().getId().equals(currentUser.getId()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Não autorizado");
        }
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        // Atualize tags similar ao create
        post.getTags().clear();
        if (dto.getTags() != null) {
            for (String tagName : dto.getTags()) {
                Tag tag = tagRepository.findByName(tagName).orElseGet(() -> tagRepository.save(new Tag(tagName)));
                post.getTags().add(tag);
            }
        }
        Post updated = postRepository.save(post);
        return mapToResponse(updated);
    }

    public void deletePost(Long id, Authentication authentication) {
        Post post = postRepository.findById(id).orElseThrow();
        User currentUser = (User) userService.loadUserByUsername(authentication.getName());
        if (!post.getAuthor().getId().equals(currentUser.getId()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Não autorizado");
        }
        postRepository.delete(post);
    }

    public Page<PostResponse> getAllPosts(Pageable pageable, String tag, String author, String search) {
        if (search != null) {
            return postRepository.searchPosts(search, pageable).map(this::mapToResponse);
        } else if (tag != null) {
            return postRepository.findByTagName(tag, pageable).map(this::mapToResponse);
        } else if (author != null) {
            return postRepository.findByAuthorUsername(author, pageable).map(this::mapToResponse);
        }
        return postRepository.findAll(pageable).map(this::mapToResponse);
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow();
        return mapToResponse(post);
    }

    private PostResponse mapToResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setPublishedDate(post.getPublishedDate());
        response.setAuthorUsername(post.getAuthor().getUsername());
        response.setTags(post.getTags());
        response.setComments(post.getComments());
        return response;
    }
}