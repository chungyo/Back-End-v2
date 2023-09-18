package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.post.PostSaveRequestDto;
import com.mmos.mmos.src.domain.entity.Notice;
import com.mmos.mmos.src.domain.entity.Post;
import com.mmos.mmos.src.domain.entity.Promotion;
import com.mmos.mmos.src.service.NoticeService;
import com.mmos.mmos.src.service.PostService;
import com.mmos.mmos.src.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController extends BaseController {

    private final PostService postService;
    private final PromotionService promotionService;
    private final NoticeService noticeService;

    @PostMapping("/{userIdx}/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> savePost(@RequestBody PostSaveRequestDto postSaveRequestDto, @PathVariable Long userIdx, @PathVariable Long studyIdx) {

        Post post;
        if(postSaveRequestDto.getIsNotice()) {
            // 공지글
            Notice notice = noticeService.saveNotice(studyIdx);
            // 글 생성
            post = postService.savePostByNotice(postSaveRequestDto, notice, userIdx);
        } else {
            // 홍보글
            Promotion promotion  = promotionService.savePromotion(studyIdx);
            // 글 생성
            post = postService.savePostByPromotion(postSaveRequestDto, promotion, userIdx);
        }

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE POST. POST_INDEX=" + post.getPost_index(), postSaveRequestDto);
    }
}
