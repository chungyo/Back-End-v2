package com.mmos.mmos.src.domain.dto.userstudy;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.entity.UserStudy;
import lombok.Getter;

@Getter
public class UserStudyResponseDto {

    private Long index;
    private Integer memberStatus;
    private HttpResponseStatus status;

    public UserStudyResponseDto(UserStudy userStudy, HttpResponseStatus status) {
        this.index = userStudy.getUserstudyIndex();
        this.memberStatus = userStudy.getUserstudyMemberStatus();
        this.status = status;
    }

    public UserStudyResponseDto(HttpResponseStatus status) {
        this.status = status;
    }
}