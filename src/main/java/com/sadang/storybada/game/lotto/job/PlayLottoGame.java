package com.sadang.storybada.game.lotto.job;

import com.sadang.storybada.game.lotto.domain.LottoBuffer;
import com.sadang.storybada.game.lotto.domain.LottoHistory;
import com.sadang.storybada.game.lotto.service.LottoBufferService;
import com.sadang.storybada.game.lotto.service.LottoHallService;
import com.sadang.storybada.game.lotto.service.LottoHistoryService;
import com.sadang.storybada.hp.service.HpService;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class PlayLottoGame {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final LottoHistoryService lottoHistoryService;
    private final LottoBufferService lottoBufferService;
    private final LottoHallService lottoHallService;
    private final HpService hpService;

    @Bean
    public Job playTheLottoGame(Step makeLottoGameResult, Step chargeLottoGameResult, Step flushLottoBuffer) {
        return new JobBuilder("playTheLottoGame", jobRepository)
                .start(makeLottoGameResult)
                .next(chargeLottoGameResult)
                .next(flushLottoBuffer)
                .build();
    }

    @Bean
    @JobScope
    public Step makeLottoGameResult(@Value("#{jobParameters[game]}") Long game) {
        return new StepBuilder("makeLottoGameResult", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                List<Integer> lottoNumberList = new ArrayList<>();
                for (int i = 1; i <= 45; i++) {
                    lottoNumberList.add(i);
                }

                Collections.shuffle(lottoNumberList);

                List<Integer> currentLottoList = lottoNumberList.subList(0, 6);
                Collections.sort(currentLottoList);
                String currentLotto = currentLottoList.toString();
                currentLotto = currentLotto.replace("[", "");
                currentLotto = currentLotto.replace("]", "");
                currentLotto = currentLotto.replace(" ", "");

                contribution.getStepExecution().getJobExecution().getExecutionContext().put("recentLottoGameResult", lottoHistoryService.addLottoHistory(game, currentLotto));

                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

    @Bean
    @JobScope
    public Step chargeLottoGameResult(@Value("#{jobParameters[game]}") Long game) {
        return new StepBuilder("chargeLottoGameResult", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                List<LottoBuffer> lottoBufferList = lottoBufferService.getAllBuffer();
                LottoHistory currentLottoHistory =contribution.getStepExecution().getJobExecution().getExecutionContext().get("recentLottoGameResult", LottoHistory.class);
                String[] currentLotto = currentLottoHistory.getLotto().split(",");

                for (LottoBuffer lottoBuffer : lottoBufferList) {
                    int count = 0;
                    long nameId = lottoBuffer.getNameId();

                    String[] playerLotto = lottoBuffer.getLotto().split(",");
                    for (String currentLottoNum : currentLotto) {
                        for (String playerLottoNum : playerLotto) {
                            if (playerLottoNum.equals(currentLottoNum)) {
                                count++;
                            }
                        }
                    }
                    int result = 7 - count;
                    if (result == 7) {
                        result = 6;
                    }

                    lottoHallService.addLottoHall(game, nameId, result);

                    switch (result) {
                        case 1:
                            hpService.addHpRecord(nameId, 1629012000L, "Lotto-1");
                            break;
                        case 2:
                            hpService.addHpRecord(nameId, 6961590,"Lotto-2");
                            break;
                        case 3:
                            hpService.addHpRecord(nameId, 146560,"Lotto-3");
                            break;
                        case 4:
                            hpService.addHpRecord(nameId, 8910,"Lotto-4");
                            break;
                        case 5:
                            hpService.addHpRecord(nameId, 1320,"Lotto-5");
                            break;
                    }

                }
                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

    @Bean
    public Step flushLottoBuffer() {
        return new StepBuilder("flushLottoBuffer", jobRepository).tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                lottoBufferService.flushLottoBuffer();

                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }


}
