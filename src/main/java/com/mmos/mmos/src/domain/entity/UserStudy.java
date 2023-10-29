package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class UserStudy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userstudyIndex;

    /*
        1: 운영진
        2: 멤버
        3: 운영진이 초대한 유저 (study -send-> user)
        4: 참가 요청한 유저 (user -send-> study)
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

    public void updateMemberStatus(Integer memberStatus){
        this.userstudyMemberStatus = memberStatus;
    }

    @Builder
    public UserStudy(Integer memberStatus, User user, Study study) {
        this.userstudyMemberStatus = memberStatus;
        this.user = user;
        this.study = study;
    }
}
