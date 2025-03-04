package com.example.project10.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@Builder
public class PostSearch {

    private static final int MAX_PAGE = 999;
    private static final int MAX_SIZE = 999;

    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;

    public PostSearch(int page, int size) {
        this.page = page - 1;
        this.size = size;
    }

    public long getOffset() {
        return (long) (page - 1) * Math.min(size, MAX_SIZE);
    }

    public Pageable getPageable() {
        return PageRequest.of(page - 1, size);
    }
}
