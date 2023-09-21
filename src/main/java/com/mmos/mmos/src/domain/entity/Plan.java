package com.mmos.mmos.src.domain.entity;

import com.mmos.mmos.src.domain.dto.plan.PlanSaveRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private Boolean planIsComplete = true;

    @Column
    private Boolean planIsStudy = false;

    @Column
    private Boolean planIsVisibleOnCalendar = false;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StudyTime> planStudytimeTimes;

    @ManyToOne
    @JoinColumn(name = "plannerIndex")
    private Planner planner;

    @ManyToOne
    @JoinColumn(name = "userstudyIndex")
    private UserStudy userStudy;

    public Plan(PlanSaveRequestDto requestDto, Planner planner, UserStudy userStudy) {
        this.planName = requestDto.getPlanName();
        this.planIsStudy = requestDto.getIsStudy();
        this.planIsVisibleOnCalendar = requestDto.getIsVisible();
        this.planner = planner;
        this.userStudy = userStudy;
    }


    public void update(String planName) {
        this.planName = planName;
    }

    public void addStudyTime(StudyTime studyTime) {
        this.planStudytimeTimes.add(studyTime);
    }
}
