package com.example.project10.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PagingResponse<T> {

    private int page;
    private int size;
    private List<T> items;

    public PagingResponse(Page<?> page, Class<T> clazz) {
        this.page = page.getNumber() + 1;
        this.size = page.getSize();
        this.items = page.getContent().stream()
                .map(content -> {
                    try {
                        return clazz.getConstructor(content.getClass()).newInstance(content);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }
}
