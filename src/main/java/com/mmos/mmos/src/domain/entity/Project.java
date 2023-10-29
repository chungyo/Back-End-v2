package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mmos.mmos.src.domain.dto.request.ProjectSaveRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectIndex;

    @Column
    private LocalDate projectStartTime;

    @Column
    private LocalDate projectEndTime;

    @Column
    private String projectName;

    @Column
    private Boolean projectIsVisible = false;

    @Column
    private Boolean projectIsComplete = false;

    @Column
    private Boolean projectIsStudy;

    @Column
    private String projectMemo;

    @Column
    private String projectPlace;

    @Column
    private Long projectNumber;

    @Column
    private Boolean projectIsAttend = false;

    @JsonBackReference
    @ManyToOne
    private Study study;

    @JsonBackReference
    @ManyToOne
    private User user;

    public void updateProjectName(String newName){
        this.projectName = newName;
    }
    public void updateProjectStartTime(LocalDate newStartTime){
        this.projectStartTime = newStartTime;
    }
    public void updateProjectEndTime(LocalDate newEndTime){
        this.projectEndTime= newEndTime;
    }
    public void updateProjectIsComplete(Boolean isComplete){
        this.projectIsComplete = isComplete;
    }
    public void updateProjectIsVisible(Boolean isVisible){
        this.projectIsVisible = isVisible;
    }

    public Project(ProjectSaveRequestDto requestDto, User user, Study study, Long projectNumber) {
        this.projectStartTime = requestDto.getStartTime();
        this.projectEndTime = requestDto.getEndTime();
        this.projectName = requestDto.getName();
        this.projectMemo = requestDto.getMemo();
        this.projectPlace = requestDto.getPlace();
        this.projectIsStudy = requestDto.getIsStudy();
        this.user = user;
        this.study = study;
        this.projectNumber = projectNumber;
    }

    public Project(ProjectSaveRequestDto requestDto, User user) {
        this.projectStartTime = requestDto.getStartTime();
        this.projectEndTime = requestDto.getEndTime();
        this.projectName = requestDto.getName();
        this.user = user;
        this.projectIsStudy = requestDto.getIsStudy();
    }

    public void setStudy(Study study) {
        this.study = study;
    }

}
