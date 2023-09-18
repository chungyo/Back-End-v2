package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.plan.PlanSaveRequestDto;
import com.mmos.mmos.src.domain.entity.Plan;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.domain.entity.UserStudy;
import com.mmos.mmos.src.repository.PlanRepository;
import com.mmos.mmos.src.repository.PlannerRepository;
import com.mmos.mmos.src.repository.UserStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
