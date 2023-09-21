package com.mmos.mmos.src.domain.entity;

import com.mmos.mmos.src.domain.dto.user.UserSaveRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@NoArgsConstructor
@DynamicInsert
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIndex;

    @Column
    private String userId;

    @Column
    private String userPassword;

    @Column
    private String userName;

    @Column
    private String userNickname;

    @Column
    @ColumnDefault("null")  // 추후에 기본 이미지 주소로 변경
    private String userProfileImage;

    @Column
    private String userEmail;

    @Column
    private Long userTotalStudyTime = 0L;

    @Column
    private Long userTotalCompletedScheduleNum = 0L;

    @Column
    private Long userStudentId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Calendar> userCalendars = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ColumnDefault("null")
    private List<UserBadge> userUserbadges = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ColumnDefault("null")
    private List<UserStudy> userUserstudies = new ArrayList<>();

    @Column
    private Boolean userStatus = true;

    @ManyToOne
    @JoinColumn(name = "universityIndex")
    private University university;

    @ManyToOne
    @JoinColumn(name = "majorIndex")
    private Major major;

    public User(UserSaveRequestDto responseDto, University university, Major major) {
        this.userId = responseDto.getId();
        this.userPassword = responseDto.getPwd();
        this.userName = responseDto.getName();
        this.userNickname = responseDto.getNickname();
        this.userEmail = responseDto.getEmail();
        this.userStudentId = responseDto.getStudentId();
        this.university = university;
        this.major = major;
    }

    public void addCalendars(Calendar calendar) {
        this.userCalendars.add(calendar);
    }

    public void addUserBadges(UserBadge userBadge) {
        this.userUserbadges.add(userBadge);
    }

    public void adduserStudies(UserStudy userStudy) {
        this.userUserstudies.add(userStudy);
    }


    public void updatePwd(String newPwd) {
        this.userPassword = newPwd;
    }

    public void updateNickname(String nickname) {
        this.userNickname = nickname;
    }

    public void updatePfp(String pfp) {
        this.userProfileImage = pfp;
    }
}