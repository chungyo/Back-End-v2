package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.src.domain.dto.request.PostSaveRequestDto;
import com.mmos.mmos.src.domain.dto.response.post.PostPageResponseDto;
import com.mmos.mmos.src.domain.entity.Post;
import com.mmos.mmos.src.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostPageController extends BaseController {

    private final PostService postService;

    @GetMapping("/{postIdx}")
    public ResponseEntity<ResponseApiMessage> getPage(@PathVariable Long postIdx) {
        try {
            Post post = postService.getPost(postIdx);
            return sendResponseHttpByJson(SUCCESS, "글 조회 성공",
                    new PostPageResponseDto(post));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    @PatchMapping("/{postIdx}/{userStudyIdx}")
    public ResponseEntity<ResponseApiMessage> updatePost(@PathVariable Long postIdx,
                                                         @PathVariable Long userStudyIdx,
                                                         @RequestBody PostSaveRequestDto requestDto) {
        try {
            return sendResponseHttpByJson(SUCCESS, "글 수정 성공",
                    postService.updatePost(postIdx, userStudyIdx, requestDto));
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    @DeleteMapping("/{postIdx}/{userStudyIdx}")
    public ResponseEntity<ResponseApiMessage> deletePost(@PathVariable Long postIdx,
                                                         @PathVariable Long userStudyIdx) {
        try {
            postService.deletePost(postIdx, userStudyIdx);
            return sendResponseHttpByJson(SUCCESS, "글 삭제 성공", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }
}
