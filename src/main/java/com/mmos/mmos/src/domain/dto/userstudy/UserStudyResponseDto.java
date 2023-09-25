package com.mmos.mmos.src.domain.dto.userstudy;

import com.mmos.mmos.src.domain.entity.UserStudy;
import lombok.Getter;

@Getter
public class UserStudyResponseDto {

    private Long index;

    private Integer memberStatus;

    public UserStudyResponseDto(UserStudy userStudy) {
        this.index = userStudy.getUserstudyIndex();
        this.memberStatus = userStudy.getUserstudyMemberStatus();
    }
}
