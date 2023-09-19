package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.entity.Notice;
import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.repository.NoticeRepositoty;
import com.mmos.mmos.src.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepositoty noticeRepositoty;
    private final StudyRepository studyRepository;

    // 스터디 공지사항 조회(단일)
    public Notice findNotice(Long noticeIdx){
        return noticeRepositoty.findById(noticeIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다. notice index = " + noticeIdx));
    }

    // 스터디 공지사항 조회(전체)
    public List<Notice> findNotices(){
        return noticeRepositoty.findAll();
    }

    public Study findStudy(Long studyIdx) {
        return studyRepository.findById(studyIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. STUDY_INDEX=" + studyIdx));
    }

    // 공지 생성
    @Transactional
    public Notice saveNotice(Long studyIdx) {
        Study study = findStudy(studyIdx);
        Notice notice = new Notice(study);

        return noticeRepositoty.save(notice);
    }

    // 스터디 공지사항 글쓰기


//    public NoticeResponseDto getNotice(Long noticeIdx) {
//        Notice notice = findNotice(noticeIdx);
//
//        return new NoticeResponseDto(notice.getNotice_index(), notice.isNotice_status(), notice.getStudy());
//    }
}
