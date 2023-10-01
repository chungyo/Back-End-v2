package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mmos.mmos.src.domain.dto.user.UserSaveRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
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
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$")
    private String userEmail;

    @Column
    private Long userTotalStudyTime = 0L;

    @Column
    private Long userTotalCompletedScheduleNum = 0L;

    @Column
    private Long userStudentId;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Calendar> userCalendars = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ColumnDefault("null")
    private List<UserBadge> userUserbadges = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ColumnDefault("null")
    private List<UserStudy> userUserstudies = new ArrayList<>();

    // 현재 친구 & 나에게 친구 요청을 한 & 내가 친구 요청을 보낸 유저들 리스트 (양방향)
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Friend> userFriends = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Project> userProjects = new ArrayList<>();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "majorIndex")
    private Major major;

    public User(UserSaveRequestDto responseDto, Major major) {
        this.userId = responseDto.getId();
        this.userPassword = responseDto.getPwd();
        this.userName = responseDto.getName();
        this.userNickname = responseDto.getNickname();
        this.userEmail = responseDto.getEmail();
        this.userStudentId = responseDto.getStudentId();
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

    public void addFriend(Friend friend) {
        this.userFriends.add(friend);
    }

    public void updatePwd(String newPwd) {
        this.userPassword = newPwd;
    }

    public void updateNickname(String nickname) {
        this.userNickname = nickname;
    }

}