package com.mmos.mmos.src.domain.dto.user;

import com.mmos.mmos.src.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSaveRequestDto {

    private String userId;
    private String userPwd;
    private String userName;
    private String userNickname;
    private Long userStudentId;


    public User toEntity() {
        return User.builder()
                .user_id(userId)
                .user_name(userName)
                .user_nickname(userNickname)
                .user_password(userPwd)
                .user_student_id(userStudentId)
                .build();
    }
}
