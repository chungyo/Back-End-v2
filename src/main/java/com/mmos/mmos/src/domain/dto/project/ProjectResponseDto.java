package com.mmos.mmos.src.domain.dto.project;

import com.mmos.mmos.src.domain.entity.Project;
import com.mmos.mmos.src.repository.ProjectRepository;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProjectResponseDto {
    private Long projectIndex;

    private LocalDate projectStartTime;

    private LocalDate projectEndTime;

    private String projectName;

    private Boolean projectIsComplete;

    public ProjectResponseDto(Project Entity) {
        this.projectIndex = Entity.getProjectIndex();
        this.projectStartTime = Entity.getProjectStartTime();
        this.projectEndTime = Entity.getProjectEndTime();
        this.projectName = Entity.getProjectName();
        this.projectIsComplete = Entity.getProjectIsComplete();
    }
}
