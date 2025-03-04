package com.example.project10.batch;

import com.example.project10.domain.Post;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;


    @Bean
    public Job logPostsJob() {
        return new JobBuilder("logPostsJob", jobRepository)
                .start(logPostsStep())
                .build();
    }

    @Bean
    public Step logPostsStep() {
        return new StepBuilder("logPostsStep", jobRepository)
                .<Post, String>chunk(5, transactionManager)
                .reader(postReader())
                .processor(postProcessor())
                .writer(postWriter())
                .build();
    }

    @Bean
    public JpaCursorItemReader<Post> postReader() {
        return new JpaCursorItemReaderBuilder<Post>()
                .name("postReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT p FROM Post p")
                .build();
    }

    @Bean
    public ItemProcessor<Post, String> postProcessor() {
        return post -> "포스트 ID: " + post.getId() + " | 제목: " + post.getTitle();
    }

    @Bean
    public ItemWriter<String> postWriter() {
        return items -> items.forEach(System.out::println);
    }

}
