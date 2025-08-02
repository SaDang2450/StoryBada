package com.sadang.storybada.game.box.job;

import com.sadang.storybada.game.box.domain.BoxBuffer;
import com.sadang.storybada.game.box.domain.BoxHall;
import com.sadang.storybada.game.box.domain.BoxHistory;
import com.sadang.storybada.game.box.service.BoxBufferService;
import com.sadang.storybada.game.box.service.BoxHallService;
import com.sadang.storybada.game.box.service.BoxHistoryService;
import com.sadang.storybada.hp.domain.Hp;
import com.sadang.storybada.hp.service.HpService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class PlayBoxGame {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final BoxBufferService boxBufferService;
    private final BoxHistoryService boxHistoryService;
    private final BoxHallService boxHallService;
    private final HpService hpService;

    @Bean
    public Job playTheBoxGame(Step isGamePlay, Step refundBox, Step makeRandomResult, Step chargeBoxGameResult, Step flushBoxBuffer) {
        return new JobBuilder("playTheBoxGame", jobRepository)
                .start(isGamePlay)
                .on("UNDER_FIVE_BOXES").to(refundBox)
                .from(refundBox).on("*").to(flushBoxBuffer)
                .from(isGamePlay).on("OVER_FIVE_BOXES").to(makeRandomResult)
                .from(makeRandomResult).on("*").to(chargeBoxGameResult)
                .from(chargeBoxGameResult).on("*").to(flushBoxBuffer)
                .end()
                .build();
    }

    @Bean
    public Step isGamePlay() {
        return new StepBuilder("isGamePlay", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                int count = boxBufferService.getAllBoxBuffer().size();
                contribution.getStepExecution().getJobExecution().getExecutionContext().put("count", count);

                if (count < 5) {
                    contribution.setExitStatus(new ExitStatus("UNDER_FIVE_BOXES"));
                } else {
                    contribution.setExitStatus(new ExitStatus("OVER_FIVE_BOXES"));
                }
                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

    @Bean
    public Step refundBox() {
        return new StepBuilder("refundBox", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                List<BoxBuffer> boxBufferList = boxBufferService.getAllBoxBuffer();
                for (BoxBuffer boxBuffer : boxBufferList) {
                    hpService.addHpRecord(boxBuffer.getNameId(), 1000, "BoxRefund");
                }

                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

    @Bean
    public Step makeRandomResult() {
        return new StepBuilder("makeRandomResult", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                int count = contribution.getStepExecution().getJobExecution().getExecutionContext().getInt("count");
                int limit = count / 5;

                List<Integer> numbers = new ArrayList<Integer>();
                for (int i = 1; i <= count; i++) {
                    numbers.add(i);
                }

                Collections.shuffle(numbers);

                String[] results = new String[5];
                results[0] = numbers.subList(0, limit).toString();
                results[1] = numbers.subList(limit, 2 * limit).toString();
                results[2] = numbers.subList(2 * limit, 3 * limit).toString();
                results[3] = numbers.subList(3 * limit, 4 * limit).toString();
                results[4] = numbers.subList(4 * limit, count).toString();

                contribution.getStepExecution().getJobExecution().getExecutionContext().put("results", results);

                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

    @Bean
    @JobScope
    public Step chargeBoxGameResult(@Value("#{jobParameters[game]}") Long game) {
        return new StepBuilder("chargeBoxGameResult", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                String[] results = contribution.getStepExecution().getJobExecution().getExecutionContext().get("results", String[].class);
                for (int i = 0; i < 5; i++) {
                    results[i] = results[i].replace("[", "");
                    results[i] = results[i].replace("]", "");
                    results[i] = results[i].replace(" ", "");
                }

                boxHistoryService.addBoxHistory(game, results);

                for (int i = 0; i < 5; i++) {
                    String[] result = results[i].split(",");
                    List<BoxHall> boxHallList = new ArrayList<>();
                    List<Hp> hpRecordList = new ArrayList<>();

                    for (String s : result) {
                        long nameId = boxBufferService.getAllBoxBuffer().get(Integer.parseInt(s) - 1).getNameId();
                        boxHallList.add(BoxHall.builder().gameId(game).nameId(nameId).result(i + 1).build());
                        hpRecordList.add(Hp.builder().nameId(nameId).point(500L * (4 - i)).game("BoxGameResult").build());
                    }
                    boxHallService.addBoxHallAll(boxHallList);
                    hpService.addHpRecordAll(hpRecordList);
                }

                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

    @Bean
    public Step flushBoxBuffer() {
        return new StepBuilder("flushBoxBuffer", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                boxBufferService.flushBoxBuffer();

                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }
}
