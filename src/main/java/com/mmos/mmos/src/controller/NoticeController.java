package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.notice.NoticeResponseDto;
import com.mmos.mmos.src.domain.entity.Notice;
import com.mmos.mmos.src.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeController extends  BaseController {
    private final NoticeService noticeService;

    @ResponseBody
    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getNotice(@RequestParam(required = false) Long noticeIdx){
        System.out.println("noticeIdx = " + noticeIdx);
        // 검색
        if(noticeIdx == null){
            List<Notice> noticeList = noticeService.findNotices();
            System.out.println("noticeList = " + noticeList);
            return sendResponseHttpByJson(SUCCESS, "Load notice list.", noticeList);
        }
        NoticeResponseDto responseDto = new NoticeResponseDto(noticeService.findNotice(noticeIdx));


        return sendResponseHttpByJson(SUCCESS, "Load Notice. NOTICE_INDEX = " + noticeIdx, responseDto);
    }



}
