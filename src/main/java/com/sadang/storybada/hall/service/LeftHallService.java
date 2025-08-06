package com.sadang.storybada.hall.service;

import com.sadang.storybada.dto.LeftHallDTO;
import com.sadang.storybada.hall.domain.Hall;
import com.sadang.storybada.hall.repository.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeftHallService {

    private final HallRepository hallRepository;

    public LocalDateTime[] getDailyRange() {
        LocalDate today = LocalDate.now();
        return new LocalDateTime[]{today.atStartOfDay(), today.plusDays(1).atStartOfDay()};
    }

    public LocalDateTime[] getWeeklyRange() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        return new LocalDateTime[]{startOfWeek.atStartOfDay(), today.plusDays(1).atStartOfDay()};
    }

    public LocalDateTime[] getMonthlyRange() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        return new LocalDateTime[]{startOfMonth.atStartOfDay(), today.plusDays(1).atStartOfDay()};
    }

    @Cacheable(value = "dailyRanking")
    public List<LeftHallDTO> getDailyRanking() {
        LocalDateTime[] range = getDailyRange();
        List<Hall> hallList = hallRepository.findTop10ByCreatedAtBetweenOrderByHpDesc(range[0], range[1]);
        List<LeftHallDTO> leftHallDTOList = new ArrayList<>();

        long ranking = 1;
        for (Hall hall : hallList) {
            leftHallDTOList.add(LeftHallDTO.builder().ranking(ranking++).name(hall.getName()).contents(hall.getContents()).build());
        }

        return leftHallDTOList;
    }

    @Cacheable(value = "weeklyRanking")
    public List<LeftHallDTO> getWeeklyRanking() {
        LocalDateTime[] range = getWeeklyRange();
        List<Hall> hallList = hallRepository.findTop10ByCreatedAtBetweenOrderByHpDesc(range[0], range[1]);
        List<LeftHallDTO> leftHallDTOList = new ArrayList<>();

        long ranking = 1;
        for (Hall hall : hallList) {
            leftHallDTOList.add(LeftHallDTO.builder().ranking(ranking++).name(hall.getName()).contents(hall.getContents()).build());
        }

        return leftHallDTOList;
    }

    @Cacheable(value = "monthlyRanking")
    public List<LeftHallDTO> getMonthlyRanking() {
        LocalDateTime[] range = getMonthlyRange();
        List<Hall> hallList = hallRepository.findTop10ByCreatedAtBetweenOrderByHpDesc(range[0], range[1]);
        List<LeftHallDTO> leftHallDTOList = new ArrayList<>();

        long ranking = 1;
        for (Hall hall : hallList) {
            leftHallDTOList.add(LeftHallDTO.builder().ranking(ranking++).name(hall.getName()).contents(hall.getContents()).build());
        }

        return leftHallDTOList;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @CacheEvict(value = "dailyRanking", allEntries = true)
    public void clearDailyRankingCache() {
        // dailyRanking 초기화
    }

    @Scheduled(cron = "0 0 0 * * MON")
    @CacheEvict(value = "weeklyRanking", allEntries = true)
    public void clearWeeklyRankingCache() {
        // weeklyRanking 초기화
    }

    @Scheduled(cron = "0 0 0 1 * *")
    @CacheEvict(value = "monthlyRanking", allEntries = true)
    public void clearMonthlyRankingCache() {
        // monthlyRanking 초기화
    }
}
