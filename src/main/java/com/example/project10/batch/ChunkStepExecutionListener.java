package com.example.project10.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChunkStepExecutionListener {

    @AfterChunk
    public void afterChunk(ChunkContext context) {
        long readCount = context.getStepContext().getStepExecution().getReadCount();
        long writeCount = context.getStepContext().getStepExecution().getWriteCount();

        log.info("청크 완료 | 읽은 항목: {} | 작성된 항목: {}", readCount, writeCount);
    }
}