package com.sadang.storybada;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
@EnableBatchProcessing
public class StoryBadaApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoryBadaApplication.class, args);
    }

}
