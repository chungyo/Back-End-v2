package com.mmos.mmos.src.domain.dto.response.planner;

import com.mmos.mmos.src.domain.entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProjectSectionDto {

    // 프로젝트 리스트
    private List<Project> project = new ArrayList<>();

    public ProjectSectionDto(List<Project> project) {
        this.project = project;
    }
}
