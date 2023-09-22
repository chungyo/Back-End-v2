package com.mmos.mmos.src.domain.dto.userstudy;

import com.mmos.mmos.src.domain.entity.UserStudy;
import lombok.Getter;

@Getter
public class UserStudyResponseDto {

    private Long index;

    private Boolean isLeader;

    private Boolean status;

    public UserStudyResponseDto(UserStudy userStudy) {
        this.index = userStudy.getUserstudyIndex();
        this.isLeader = userStudy.getUserstudyIsLeader();
        this.status = userStudy.getUserstudyStatus();
    }
}
