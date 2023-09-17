package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.entity.Notice;
import com.mmos.mmos.src.repository.NoticeRepositoty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepositoty noticeRepositoty;

    // 스터디 공지사항 조회(단일)
    public Notice findNotice(Long noticeIdx){
        return noticeRepositoty.findById(noticeIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다. notice index = " + noticeIdx));
    }

    // 스터디 공지사항 조회(전체)
    public List<Notice> findNotices(){
        return noticeRepositoty.findAll();
    }

    // 스터디 공지사항 글쓰기


//    public NoticeResponseDto getNotice(Long noticeIdx) {
//        Notice notice = findNotice(noticeIdx);
//
//        return new NoticeResponseDto(notice.getNotice_index(), notice.isNotice_status(), notice.getStudy());
//    }
}
