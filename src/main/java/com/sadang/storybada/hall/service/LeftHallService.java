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
        LocalDateTime now = LocalDateTime.now();
        return new LocalDateTime[]{today.atStartOfDay(), now};
    }

    public LocalDateTime[] getWeeklyRange() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDateTime now = LocalDateTime.now();
        return new LocalDateTime[]{startOfWeek.atStartOfDay(), now};
    }

    public LocalDateTime[] getMonthlyRange() {
        LocalDate today = LocalDate.now().minusDays(1);
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDateTime now = LocalDateTime.now();
        return new LocalDateTime[]{startOfMonth.atStartOfDay(), now};
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

    @Scheduled(fixedRate = 10 * 60 * 1000)
    @CacheEvict(value = {"dailyRanking", "weeklyRanking", "monthlyRanking"}, allEntries = true)
    public void clearRankingCache() {
        // Ranking Cache 초기화
    }
}
