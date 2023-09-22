package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.plan.PlanNameUpdateRequestDto;
import com.mmos.mmos.src.domain.dto.plan.PlanResponseDto;
import com.mmos.mmos.src.domain.dto.plan.PlanSaveRequestDto;
import com.mmos.mmos.src.domain.entity.Plan;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.domain.entity.UserStudy;
import com.mmos.mmos.src.repository.PlanRepository;
import com.mmos.mmos.src.repository.PlannerRepository;
import com.mmos.mmos.src.repository.UserStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlannerRepository plannerRepository;
    private final UserStudyRepository userStudyRepository;

    public Planner findPlanner(Long plannerIdx) {
        return plannerRepository.findById(plannerIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 플래너입니다. PLANNER_INDEX=" + plannerIdx));
    }

    public UserStudy findUserStudy(Long userStudyIdx) {
        return userStudyRepository.findById(userStudyIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자가 가입된 스터디입니다. USERSTUDY_INDEX=" + userStudyIdx));
    }

    public Plan findPlan(Long planIdx) {
        return planRepository.findById(planIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 플랜입니다. PLAN_INDEX=" + planIdx));
    }

    public List<Plan> findPlans() {
        return planRepository.findAll();
    }

    public void savePlan(PlanSaveRequestDto requestDto, Long plannerIdx) {
        System.out.println("requestDto = " + requestDto.getIsStudy());
        // 플래너 가져오기
        Planner planner = findPlanner(plannerIdx);
        UserStudy userStudy = null;
        if(requestDto.getIsStudy()) {
            userStudy = findUserStudy(requestDto.getUserStudyIdx());
        }

        // Plan 객체 생성
        Plan plan = new Plan(requestDto, planner, userStudy);

        // 역 FK 매핑
        planner.addPlan(plan);

        planRepository.save(plan);
    }

    @Transactional
    public PlanResponseDto getPlan(Long planIdx) {
        Plan plan = findPlan(planIdx);

        return new PlanResponseDto(plan);
    }

    @Transactional
    public List<PlanResponseDto> getPlans() {
        List<PlanResponseDto> responseDtoList = new ArrayList<>();
        List<Plan> plans = findPlans();

        for (Plan plan : plans) {
            responseDtoList.add(new PlanResponseDto(plan));
        }

        return responseDtoList;
    }

    @Transactional
    public PlanResponseDto updatePlan(Long planIdx, PlanNameUpdateRequestDto requestDto) {
        Plan plan = findPlan(planIdx);

        plan.update(requestDto.getPlanName());

        return new PlanResponseDto(plan);
    }
}
