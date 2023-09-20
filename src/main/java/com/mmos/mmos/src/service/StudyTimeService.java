package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.studytime.StudyTimeResponseDto;
import com.mmos.mmos.src.domain.entity.Plan;
import com.mmos.mmos.src.domain.entity.StudyTime;
import com.mmos.mmos.src.repository.PlanRepository;
import com.mmos.mmos.src.repository.StudyTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class StudyTimeService {

    private final StudyTimeRepository studyTimeRepository;
    private final PlanRepository planRepository;

    public Plan findPlan(Long planIdx) {
        return planRepository.findById(planIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계획입니다. PLAN_INDEX=" + planIdx));
    }

    // save
    @Transactional
    public StudyTimeResponseDto setStartTime(Long planIdx) {
        Plan plan = findPlan(planIdx);


        // 가장 최근 스터디 리스트에 마감을 하지 않은게 있다면 돌아가지 않도록
        for (Plan plannerPlan : plan.getPlanner().getPlanner_plans()) {
            if(plannerPlan.getPlan_studytime_times().isEmpty())
                continue;
            if(plannerPlan.getPlan_studytime_times().get(plannerPlan.getPlan_studytime_times().size() - 1).getStudytime_end_time() == null)
                return null;
        }

        StudyTime studyTime = studyTimeRepository.save(new StudyTime(new Timestamp(System.currentTimeMillis()), null, plan));
        plan.addStudyTime(studyTime);

        return new StudyTimeResponseDto(studyTime.getStudytime_index(), studyTime.getStudytime_start_time(), studyTime.getStudytime_end_time());
    }

    // update
    @Transactional
    public StudyTimeResponseDto setEndTime(Long planIdx) {
        Plan plan = findPlan(planIdx);

        if(plan.getPlan_studytime_times().isEmpty() || plan.getPlan_studytime_times().get(plan.getPlan_studytime_times().size() - 1).getStudytime_end_time() != null)
            return null;

        StudyTime studyTime = plan.getPlan_studytime_times().get(plan.getPlan_studytime_times().size() - 1);
        studyTime.updateEndTime(new Timestamp(System.currentTimeMillis()));

        return new StudyTimeResponseDto(studyTime.getStudytime_index(), studyTime.getStudytime_start_time(), studyTime.getStudytime_end_time());
    }


}

