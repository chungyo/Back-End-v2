package com.mmos.mmos.src.domain.dto.planner;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.entity.Plan;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.domain.entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class PlannerResponseDto {

    private Long idx;
    private LocalDate date;
    private String memo;
    private Long dailyStudyTime;
    private Long dailyScheduleNum;
    private List<Plan> plans;
    private List<Project> projects;
    private HttpResponseStatus status = null;

    public PlannerResponseDto(Planner planner, List<Project> projects, HttpResponseStatus status) {
        this.idx = planner.getPlannerIndex();
        this.date = planner.getPlannerDate();
        this.memo = planner.getPlannerMemo();
        this.dailyStudyTime = planner.getPlannerDailyStudyTime();
        this.dailyScheduleNum = planner.getPlannerDailyScheduleNum();
        this.projects = projects;
        this.status = status;
        this.plans = planner.getPlannerPlans();


    }


    public PlannerResponseDto(HttpResponseStatus status){
        this.idx = null;
        this.date = null;
        this.memo = null;
        this.dailyStudyTime = null;
        this.dailyScheduleNum = null;
        this.status = status;
        this.projects = null;
        this.plans = null;
    }
}
