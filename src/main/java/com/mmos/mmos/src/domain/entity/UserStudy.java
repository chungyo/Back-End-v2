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
    private Long userstudyIndex;


    @Column
    private Boolean userstudyIsLeader = false;

    @Column
    private Boolean userstudyStatus = true;

    @ManyToOne
    @JoinColumn(name = "userIndex")
    private User user;

    @ManyToOne
    @JoinColumn(name = "studyIndex")
    private Study study;

    @OneToMany(mappedBy = "userStudy", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Plan> userstudyPlans = new ArrayList<>();

    public void leaderUpdate(Boolean isLeader){
        this.userstudyIsLeader = isLeader;
    }

    @Builder
    public UserStudy(Boolean userstudyIsLeader, User user, Study study) {
        this.userstudyIsLeader = userstudyIsLeader;
        this.user = user;
        this.study = study;
    }
}
