package com.mmos.mmos.src.domain.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    // 이메일 인증이 완료됐는가
    private Boolean isCertified;
    // 아이디
    private String id;
    // 비밀번호
    private String pwd;
    // 이메일
    private String email;
    // 전공 인덱스
    private Long majorIdx;
    // 이름
    private String name;

    public void encryptPwd(String pwd) {
        this.pwd = pwd;
    }
}
