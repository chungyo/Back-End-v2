package com.mmos.mmos.src.domain.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdatePwdRequestDto {
    private String prevPwd;
    private String newPwd;
    private String checkPwd;
}
