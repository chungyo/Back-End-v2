package com.mmos.mmos.src.domain.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPwdUpdateDto {

    private String prevPwd;
    private String newPwd;
    private String newPwdByCheck;

}
