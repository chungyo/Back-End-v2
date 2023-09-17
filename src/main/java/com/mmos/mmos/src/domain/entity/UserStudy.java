package com.mmos.mmos.src.domain.entity;

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
    private List<Plan> userstudy_plans;
}
