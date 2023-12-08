package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mmos.mmos.src.domain.dto.request.SignUpRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIndex;

    @Column
    @Pattern(regexp = "^[A-Za-z0-9]{5,20}$")
    private String userId;

    @Column
    private String userPassword;

    @Column
    @Pattern(regexp = "^[가-힣A-Za-z ]{2,20}$")
    private String userName;



    public String getUserName() {
        return userName;
    }

    @Column
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$")
    private String userEmail;

    @Column
    @ColumnDefault(value = "0")
    private Long userTotalStudyTime = 0L;

    @Column
    @ColumnDefault(value = "0")
    private Long userWeeklyStudyTime = 0L;

    @Column
    @ColumnDefault(value = "0")
    private Long userTotalCompletedScheduleNum = 0L;

    @Column
    @ColumnDefault(value = "0")
    private Long userTotalScheduleNum = 0L;

    @Column
    @ColumnDefault(value = "0")
    private Long userCurrentStreak = 0L;

    @Column
    @ColumnDefault(value = "0")
    private Long userTopStreak = 0L;

    @Column
    private Boolean isPlannerVisible = true;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Streak> streaks = new ArrayList<>();

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

    public User(SignUpRequestDto responseDto, Major major) {
        this.userId = responseDto.getId();
        this.userPassword = responseDto.getPwd();
        this.userName = responseDto.getName();
        this.userEmail = responseDto.getEmail();
        this.major = major;
    }

    public String getName() {
        return userName;
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

    public void addProject(Project project) {
        this.userProjects.add(project);
    }

    public void addTotalTime(Long time) {
        this.userTotalStudyTime += time;
    }

    public void minusTotalTime(Long time) {
        this.userTotalStudyTime -= time;
    }

    public void addWeeklyTime(Long time) {
        this.userWeeklyStudyTime += time;
    }

    public void minusWeeklyTime(Long time) {
        this.userWeeklyStudyTime -= time;
    }

    public void updatePwd(String newPwd) {
        this.userPassword = newPwd;
    }

    public void updateTopStreak(Long days) {
        this.userTopStreak = days;
    }

    public void updateName(String name) {
        this.userName = name;
    }

    public void updateTotalSchedule(boolean status) {
        if(status){
            this.userTotalScheduleNum++;
        }
        else{
            this.userTotalScheduleNum--;
        }
    }

    public void updateTotalCompleteSchedule(boolean status) {
        if(status){
            this.userTotalCompletedScheduleNum++;
        }
        else{
            this.userTotalCompletedScheduleNum--;
        }
    }

    public void updateIsPlannerVisible(boolean status) {
        this.isPlannerVisible = status;
    }


    public void plusCurrentStreak() {
        this.userCurrentStreak++;
    }

    public void resetCurrentStreak() {
        this.userCurrentStreak = 0L;
    }

    public void addStreak(Streak streak) {
        this.streaks.add(streak);
    }

    public void updateId(String id) {
        this.userId = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}