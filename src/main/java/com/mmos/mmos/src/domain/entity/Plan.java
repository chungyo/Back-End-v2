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
    private Long plan_index;

    @Column
    private String plan_name;

    @Column
    private Boolean plan_is_complete = true;

    @Column
    private Boolean plan_is_study = false;

    @Column
    private Boolean plan_is_visible_on_calendar = false;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StudyTime> plan_studytime_times;

    @Column
    private Boolean plan_status = true;

    @ManyToOne
    @JoinColumn(name = "planner_index")
    private Planner planner;

    @ManyToOne
    @JoinColumn(name = "userstudy_index")
    private UserStudy userStudy;

    public Plan(PlanSaveRequestDto requestDto, Planner planner, UserStudy userStudy) {
        this.plan_name = requestDto.getPlanName();
        this.plan_is_study = requestDto.getIsStudy();
        this.plan_is_visible_on_calendar = requestDto.getIsVisible();
        this.planner = planner;
        this.userStudy = userStudy;
    }


    public void update(String planName) {
        this.plan_name = planName;
    }

    public void addStudyTime(StudyTime studyTime) {
        this.getPlan_studytime_times().add(studyTime);
    }
}
