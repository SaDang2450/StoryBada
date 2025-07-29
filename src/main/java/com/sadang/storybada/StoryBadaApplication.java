package com.sadang.storybada;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class StoryBadaApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoryBadaApplication.class, args);
    }

}
