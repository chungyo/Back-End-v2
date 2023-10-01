package com.mmos.mmos.src.domain.dto.project;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.entity.Project;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProjectResponseDto {
    private Long projectIndex;

    private LocalDate projectStartTime;

    private LocalDate projectEndTime;

    private String projectName;

    private Boolean projectIsComplete;
    private Boolean projectIsVisible;
    private HttpResponseStatus status;

    public ProjectResponseDto(Project Entity, HttpResponseStatus status) {
        this.projectIndex = Entity.getProjectIndex();
        this.projectStartTime = Entity.getProjectStartTime();
        this.projectEndTime = Entity.getProjectEndTime();
        this.projectName = Entity.getProjectName();
        this.projectIsComplete = Entity.getProjectIsComplete();
        this.projectIsVisible = Entity.getProjectIsVisible();
        this.status = status;
    }

    public ProjectResponseDto(HttpResponseStatus status) {
        this.projectIndex = null;
        this.projectStartTime = null;
        this.projectEndTime = null;
        this.projectName = null;
        this.projectIsComplete = null;
        this.projectIsVisible = null;
        this.status = status;
    }
}
