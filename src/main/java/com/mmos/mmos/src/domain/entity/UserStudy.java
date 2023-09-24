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

    // Userstudy.isMember : 1 == leader, 2 == manager, 3 == member, 4 == application, 5 == invitee
    @Column
    private Integer userstudyMemberStatus;

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

    public void updateMemberStatus(Integer memberStatus){
        this.userstudyMemberStatus = memberStatus;
    }

    @Builder
    public UserStudy(Integer userstudyIsMember, User user, Study study) {
        this.userstudyMemberStatus = userstudyIsMember;
        this.user = user;
        this.study = study;
    }
}
