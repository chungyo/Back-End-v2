package com.mmos.mmos.src.domain.dto.response;

import com.mmos.mmos.src.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyPageResponseDto {
    private Long userIdx;
    private String pfp;
    private String id;
    private String name;
    private String universityName;
    private String majorName;
    private String email;
    private boolean isPublic;

    public MyPageResponseDto(User user, String pfp) {
        this.userIdx = user.getUserIndex();
        this.pfp = pfp;
        this.id = user.getUserId();
        this.name = user.getName();
        this.universityName = user.getMajor().getCollege().getUniversity().getUniversityName();
        this.majorName = user.getMajor().getMajorName();
        this.email = user.getUserEmail();
        this.isPublic = user.getIsPlannerVisible();
    }
}
