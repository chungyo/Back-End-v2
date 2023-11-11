package com.mmos.mmos.src.domain.entity.primary;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mmos.mmos.src.domain.dto.request.PlanSaveRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.sql.Timestamp;

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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "plannerIndex")
    private Planner planner;

    @Column
    @ColumnDefault("0")
    private Long planStudyTime;

    @Column
    private Timestamp studytimeStartTime;

    public Plan(PlanSaveRequestDto requestDto, Planner planner) {
        this.planName = requestDto.getPlanName();
        this.planner = planner;
    }


    public void updateName(String planName) {
        this.planName = planName;
    }

    public void updateIsComplete(Boolean planIsComplete) {
        this.planIsComplete = planIsComplete;
    }

    public void setStudyTime(Long time) {
        this.planStudyTime += time;
    }

    public void setStartTime(Timestamp now) {
        this.studytimeStartTime = now;
    }

    public void resetTime() {
        this.studytimeStartTime = null;
    }
}
