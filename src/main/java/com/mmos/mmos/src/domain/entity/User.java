package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_index;

    @Column
    private String user_id;

    @Column
    private String user_password;

    @Column
    private String user_name;

    @Column
    private String user_nickname;

    @Column
    private String user_profile_image;

    @Column
    @ColumnDefault("0")
    private Long user_total_study_time;

    @Column
    @ColumnDefault("0")
    private Integer user_tier;

    @Column
    private Long user_student_id;

    @Column
    @ColumnDefault("0")
    private Long user_total_completed_schedule_num;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Planner> user_planners;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ColumnDefault("null")
    private List<UserBadge> user_userbadges;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ColumnDefault("null")
    private List<UserStudy> user_studies;

    @Column
    @ColumnDefault("true")
    private boolean user_status;

    @ManyToOne
    @JoinColumn(name = "university_index")
    private University university;

}