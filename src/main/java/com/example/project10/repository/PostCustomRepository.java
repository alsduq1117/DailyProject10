package com.example.project10.repository;

import com.example.project10.domain.Post;
import com.example.project10.request.PostSearch;
import org.springframework.data.domain.Page;

public interface PostCustomRepository {

    Page<Post> getList(PostSearch postSearch);
}
