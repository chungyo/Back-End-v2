package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Boolean projectIsComplete = false;

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

    public Project(LocalDate projectStartTime, LocalDate projectEndTime, String projectName, User user) {
        this.projectStartTime = projectStartTime;
        this.projectEndTime = projectEndTime;
        this.projectName = projectName;
        this.user = user;
    }
}
