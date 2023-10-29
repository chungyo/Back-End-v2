package com.mmos.mmos.src.domain.dto.response.study;

import com.mmos.mmos.src.domain.entity.Study;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class MyStudySectionDto {
    // 스터디 인덱스
    Long idx;
    // 스터디 이름
    String name;
    // 스터디 메모
    String memo;
    // 스터디 일정
    Page<ProjectSectionDto> projectSection;
    // 스터디원 목록
    StudyMemberSectionDto memberSection;
    // 공지사항
    Page<NoticeSectionDto> notices;

    public MyStudySectionDto(Study study,
                             Page<ProjectSectionDto> projectSection,
                             StudyMemberSectionDto memberSection,
                             Page<NoticeSectionDto> notices) {
        this.idx = study.getStudyIndex();
        this.name = study.getStudyName();
        this.memo = study.getStudyMemo();
        this.projectSection = projectSection;
        this.memberSection= memberSection;
        this.notices = notices;
    }
}
