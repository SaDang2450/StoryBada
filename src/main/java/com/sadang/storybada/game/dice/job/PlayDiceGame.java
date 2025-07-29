package com.sadang.storybada.game.dice.job;

import com.sadang.storybada.game.dice.service.DiceBufferService;
import com.sadang.storybada.game.dice.service.DiceHallService;
import com.sadang.storybada.game.dice.service.DiceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class PlayDiceGame {

    private final DiceHistoryService diceHistoryService;
    private final DiceHallService diceHallService;
    private final DiceBufferService diceBufferService;

    public boolean makeRandomDiceResult() {
        Random random = new Random();

        return random.nextBoolean();
    }

//    @Bean
//    public Job playDiceGame() {
//        return this.jobBuilderFactory.get("playDiceGame").start(add);
//    }

    @Bean
    public Step makeDiceGameResult() {
        return StepBuilder.get("makeDiceGameResult").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                diceHistoryService.addDiceHistory(1, makeRandomDiceResult());
                return RepeatStatus.FINISHED;
            }
        }).build();
    }

}
