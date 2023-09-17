package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@ToString
@DynamicInsert
public class UserStudy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userstudy_index;


    @Column
    private boolean userstudy_is_leader = false;
    @Column
    private boolean userstudy_status = true;

    @ManyToOne
    @JoinColumn(name = "user_index")
    private User user;

    @ManyToOne
    @JoinColumn(name = "study_index")
    private Study study;

    @OneToMany(mappedBy = "userStudy", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Plan> userstudy_plans = new ArrayList<>();

    @Builder
    public UserStudy(boolean userstudy_is_leader, User user, Study study) {
        this.userstudy_is_leader = userstudy_is_leader;
        this.user = user;
        this.study = study;
    }
}
