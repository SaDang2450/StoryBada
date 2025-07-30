package com.sadang.storybada.game.dice.job;

import com.sadang.storybada.game.dice.domain.DiceBuffer;
import com.sadang.storybada.game.dice.domain.DiceHistory;
import com.sadang.storybada.game.dice.service.DiceBufferService;
import com.sadang.storybada.game.dice.service.DiceHallService;
import com.sadang.storybada.game.dice.service.DiceHistoryService;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.user.service.UserService;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class PlayDiceGame {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DiceHistoryService diceHistoryService;
    private final DiceHallService diceHallService;
    private final DiceBufferService diceBufferService;
    private final HpService hpService;
    private final UserService userService;

    public boolean makeRandomDiceResult() {
        Random random = new Random();

        return random.nextBoolean();
    }

    @Bean
    public Job playTheDiceGame(Step makeDiceGameResult, Step chargeDiceGameResult, Step flushDiceBuffer) {
        return new JobBuilder("playTheDiceGame", jobRepository)
                .start(makeDiceGameResult)
                .next(chargeDiceGameResult)
                .next(flushDiceBuffer)
                .build();
    }

    @Bean
    @JobScope
    public Step makeDiceGameResult(@Value("#{jobParameters[game]}") Long game) {
        return new StepBuilder("makeDiceGameResult", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                contribution.getStepExecution().getJobExecution().getExecutionContext()
                        .put("recentDiceGameResult", diceHistoryService.addDiceHistory(game, makeRandomDiceResult()));
                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

    @Bean
    @JobScope
    public Step chargeDiceGameResult(@Value("#{jobParameters[game]}") Long game) {
        return new StepBuilder("chargeDiceGameResult", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                List<DiceBuffer> diceBufferList = diceBufferService.getAllBuffer();
                DiceHistory recentDiceHistory = contribution.getStepExecution().getJobExecution().getExecutionContext()
                        .get("recentDiceGameResult", DiceHistory.class);

                long recentGameNumber = recentDiceHistory.getGame();
                boolean recentResult = recentDiceHistory.isResult();

                for (DiceBuffer diceBuffer : diceBufferList) {
                    long nameId = diceBuffer.getNameId();
                    long hp = diceBuffer.getHp();
                    boolean result = diceBuffer.isResult();

                    if (result == recentResult) {
                        diceHallService.addDiceHall(recentGameNumber, nameId, true);
                        hpService.addHpRecord(nameId, hp * 2, "DiceGame");
                    } else {
                        diceHallService.addDiceHall(recentGameNumber, nameId, false);
                    }
                }

                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

    @Bean
    public Step flushDiceBuffer() {
        return new StepBuilder("flushDiceBuffer", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                diceBufferService.flushDiceBuffer();

                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

}

