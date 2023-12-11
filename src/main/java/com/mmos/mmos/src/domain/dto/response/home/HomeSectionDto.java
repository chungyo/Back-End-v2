package com.mmos.mmos.src.domain.dto.response.home;

import com.mmos.mmos.src.domain.entity.Badge;
import com.mmos.mmos.src.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class HomeSectionDto {

    // 아아디
    private String id;
    // 이름
    private String name;
    // 프사
    private String pfp;
    // 학교
    private String universityName;
    // 단과대
    private String collegeName;
    // 전공
    private String majorName;
    // 도전과제 뱃지 이미지
    private List<String> badgeIcon = new ArrayList<>();

    public HomeSectionDto(User user, List<Badge> badges, Badge pfp) {
        this.id = user.getUserId();
        this.name = user.getName();
        this.pfp = pfp.getBadgeIcon();
        this.universityName = user.getMajor().getCollege().getUniversity().getUniversityName();
        this.collegeName = user.getMajor().getCollege().getCollegeName();
        this.majorName = user.getMajor().getMajorName();
        for (Badge badge : badges) {
            badgeIcon.add(badge.getBadgeIcon());
        }
    }
}
