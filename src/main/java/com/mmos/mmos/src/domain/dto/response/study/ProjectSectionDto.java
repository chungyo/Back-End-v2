package com.mmos.mmos.src.domain.dto.response.study;

import com.mmos.mmos.src.domain.entity.Project;
import com.mmos.mmos.src.domain.entity.Study;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectSectionDto {
    // 제목
    private String name;
    // 시작 날짜
    private LocalDate startDate;
    // 마감 날짜
    private LocalDate endDate;
    // 장소
    private String place;
    // 메모
    private String memo;
    // 멤버 리스트
    private List<MemberSectionInProjectDto> members = new ArrayList<>();


    public ProjectSectionDto(Project studyProject,
                             Study study,
                             List<MemberSectionInProjectDto> members) {
        this.name = studyProject.getProjectName();
        this.startDate = studyProject.getProjectStartTime();
        this.endDate = studyProject.getProjectEndTime();
        this.place = studyProject.getProjectPlace();
        this.memo = studyProject.getProjectMemo();
        this.members = members;
    }
}
