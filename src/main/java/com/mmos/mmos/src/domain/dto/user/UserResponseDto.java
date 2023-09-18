package com.mmos.mmos.src.domain.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserResponseDto {

    private Long userIdx;
    private String userId;
    private String userPwd;
    private String userName;
    private String userNickname;
    private Long userStudentId;
    private Integer userTier;
    private Long userTotalStudyTime;
    private String userPfp;
    private Boolean userStatus;

}

