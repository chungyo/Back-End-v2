package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    /*
        1: 운영진
        2: 부운영진
        3: 멤버
        4: 운영진이 초대한 유저 (study -send-> user)
        5: 참가 요청한 유저 (user -send-> study)
     */
    @Column
    private Integer userstudyMemberStatus;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userIndex")
    private User user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "studyIndex")
    private Study study;

    @JsonManagedReference
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
