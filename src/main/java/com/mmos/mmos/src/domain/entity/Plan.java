package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mmos.mmos.src.domain.dto.plan.PlanSaveRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planIndex;

    @Column
    private String planName;

    @Column
    private Boolean planIsComplete = false;

    @Column
    private Boolean planIsStudy = false;

    @JsonManagedReference
    @OneToMany(mappedBy = "plan", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StudyTime> planStudytimeTimes;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "plannerIndex")
    private Planner planner;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userstudyIndex")
    private UserStudy userStudy;

    @Column
    @ColumnDefault("0")
    private Long planStudyTime;

    public Plan(PlanSaveRequestDto requestDto, Planner planner, UserStudy userStudy) {
        this.planName = requestDto.getPlanName();
        this.planIsStudy = requestDto.getIsStudy();
        this.planner = planner;
        this.userStudy = userStudy;
    }


    public void update(String planName) {
        this.planName = planName;
    }

    public void updateIsComplete(Boolean planIsComplete) {
        this.planIsComplete = planIsComplete;
    }

    public void addStudyTime(StudyTime studyTime) {
        this.planStudytimeTimes.add(studyTime);
    }

    public void addTime(Long time) {
        this.planStudyTime += time;
    }

}
