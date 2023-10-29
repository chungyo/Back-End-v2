package com.mmos.mmos.src.domain.dto.response.planner;

import com.mmos.mmos.src.domain.dto.response.home.CalendarSectionDto;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.domain.entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PlannerPageResponseDto {

    CalendarSectionDto calendar;
    PlannerSectionDto planner;
    ProjectSectionDto project;

    public PlannerPageResponseDto(CalendarSectionDto calendar, Planner planner, List<Project> projects) {
        // Calendar Section
        this.calendar = calendar;

        // Planner Section
        this.planner = new PlannerSectionDto(planner);

        // Project Section
        this.project = new ProjectSectionDto(projects);
    }

    public PlannerPageResponseDto(CalendarSectionDto calendar, List<Project> projects) {
        this.calendar = calendar;
        this.project = new ProjectSectionDto(projects);
    }
}
