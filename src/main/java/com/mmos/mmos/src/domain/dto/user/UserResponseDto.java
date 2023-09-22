package com.mmos.mmos.src.domain.dto.user;

import com.mmos.mmos.src.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    private Long idx;
    private String id;
    private String pwd;
    private String name;
    private String nickname;
    private String email;
    private Long totalStudyTime;
    private Long totalSchedule;
    private Long studentId;
    private Boolean userStatus;

    public UserResponseDto(User user) {
        this.idx = user.getUserIndex();
        this.id = user.getUserId();
        this.pwd = user.getUserPassword();
        this.name = user.getUserName();
        this.nickname = user.getUserNickname();
        this.email = user.getUserEmail();
        this.totalStudyTime = user.getUserTotalStudyTime();
        this.totalSchedule = user.getUserTotalCompletedScheduleNum();
        this.studentId = user.getUserStudentId();
        this.userStatus = getUserStatus();
    }
}

