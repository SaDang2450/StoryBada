package com.sadang.storybada.game.dice.job;

import com.sadang.storybada.game.dice.domain.DiceBuffer;
import com.sadang.storybada.game.dice.domain.DiceHistory;
import com.sadang.storybada.game.dice.service.DiceBufferService;
import com.sadang.storybada.game.dice.service.DiceHallService;
import com.sadang.storybada.game.dice.service.DiceHistoryService;
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

    public boolean makeRandomDiceResult() {
        Random random = new Random();

        return random.nextBoolean();
    }

    @Bean
    public Job playTheDiceGame(Step makeDiceGameResult, Step readDiceGameBuffer, Step chargeDiceGameResult, Step flushDiceBuffer) {
        return new JobBuilder("playTheDiceGame", jobRepository)
                .start(makeDiceGameResult)
                .next(readDiceGameBuffer)
                .on("REPEAT_STEP").to(chargeDiceGameResult).next(readDiceGameBuffer)
                .from(readDiceGameBuffer).on("END_STEP").to(flushDiceBuffer)
                .end().build();
    }

    @Bean
    @JobScope
    public Step makeDiceGameResult(@Value("#{jobParameters[game]}") Long game) {
        return new StepBuilder("makeDiceGameResult", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                DiceHistory recent = diceHistoryService.addDiceHistory(game, makeRandomDiceResult());
                contribution.getStepExecution().getJobExecution().getExecutionContext()
                        .put("recentDiceGameResult", recent);
                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

    @Bean
    @JobScope
    public Step readDiceGameBuffer() {
        return new StepBuilder("readDiceGameBuffer", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                List<DiceBuffer> diceBufferList = diceBufferService.getDiceBuffer500();
                contribution.getStepExecution().getJobExecution().getExecutionContext()
                        .put("diceBufferList", new ArrayList<>(diceBufferList));

                if (diceBufferList.isEmpty()) {
                    contribution.setExitStatus(new ExitStatus("END_STEP"));
                } else {
                    contribution.setExitStatus(new ExitStatus("REPEAT_STEP"));
                }

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

                DiceHistory recentDiceHistory = contribution.getStepExecution().getJobExecution().getExecutionContext()
                        .get("recentDiceGameResult", DiceHistory.class);

                @SuppressWarnings("unchecked")
                List<DiceBuffer> diceBufferList = (List<DiceBuffer>) contribution.getStepExecution()
                        .getJobExecution()
                        .getExecutionContext()
                        .get("diceBufferList");

                if (diceBufferList == null) diceBufferList = new ArrayList<>();

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

                diceBufferService.deleteDiceBufferByList(diceBufferList);

                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

    @Bean
    public Step flushDiceBuffer() {
        return new StepBuilder("flushDiceBuffer", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

}

