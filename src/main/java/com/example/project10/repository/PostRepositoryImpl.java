package com.example.project10.repository;

import com.example.project10.domain.Post;
import com.example.project10.domain.QPost;
import com.example.project10.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> getList(PostSearch postSearch) {
        QPost post = QPost.post;
        long totalCount = jpaQueryFactory.select(post.count())
                .from(post)
                .fetchFirst();

        List<Post> postList = jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(post.id.desc())
                .fetch();

        return new PageImpl<>(postList, postSearch.getPageable(), totalCount);
    }
}
