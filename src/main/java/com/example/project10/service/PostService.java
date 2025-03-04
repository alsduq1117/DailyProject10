package com.example.project10.service;

import com.example.project10.domain.Post;
import com.example.project10.domain.PostEditor;
import com.example.project10.exception.PostNotFound;
import com.example.project10.repository.PostRepository;
import com.example.project10.request.PostCreate;
import com.example.project10.request.PostEdit;
import com.example.project10.request.PostSearch;
import com.example.project10.response.PagingResponse;
import com.example.project10.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostResponse write(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);

        return PostResponse.from(post);
    }

    @Cacheable(value = "PostCache", key = "#postId")
    public PostResponse get(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);

        return PostResponse.from(post);
    }

    public PagingResponse<PostResponse> getList(PostSearch postSearch) {
        Page<Post> postPage = postRepository.getList(postSearch);
        return new PagingResponse<>(postPage, PostResponse.class);
    }

    @Transactional
    @CachePut(value = "PostCache", key = "#postId")
    public PostResponse edit(Long postId, PostEdit postEdit) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();
        PostEditor postEditor = postEditorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);

        return PostResponse.from(post);
    }


    @Transactional
    @CacheEvict(value = "PostCache", key = "#postId")
    public void delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);
        postRepository.delete(post);
    }
}
