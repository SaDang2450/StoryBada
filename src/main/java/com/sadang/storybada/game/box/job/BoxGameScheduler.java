package com.sadang.storybada.game.box.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BoxGameScheduler {

    private final JobLauncher jobLauncher;
    private final Job playTheBoxGame;

    @Scheduled(cron = "20 * * * * *", zone = "Asia/Seoul")
    public void runBoxGame() throws JobParametersInvalidException, JobExecutionAlreadyRunningException {
        LocalDateTime now = LocalDateTime.now();
        long hour = now.getHour();
        long minute = now.getMinute();

        long game = hour * 60 + minute + 1;

        JobParameters jobParameters = new JobParametersBuilder().addLong("game", game).toJobParameters();

        try {
            jobLauncher.run(playTheBoxGame, jobParameters);
        } catch (JobInstanceAlreadyCompleteException | JobRestartException e) {
            throw new RuntimeException(e);
        }
    }
}
