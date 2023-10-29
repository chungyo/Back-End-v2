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
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostPageController extends BaseController {

    private final PostService postService;

    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getPage(@RequestParam Long postIdx) {
        try {
            Post post = postService.getPost(postIdx);
            return sendResponseHttpByJson(SUCCESS, "글 조회 성공",
                    new PostPageResponseDto(post));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    @PostMapping("")
    public ResponseEntity<ResponseApiMessage> savePost(@RequestParam Long userStudyIdx,
                                                       @RequestBody PostSaveRequestDto requestDto) {
        try {
            return sendResponseHttpByJson(SUCCESS, "글 쓰기 성공",
                    postService.savePost(userStudyIdx, requestDto));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    @PatchMapping("")
    public ResponseEntity<ResponseApiMessage> updatePost(@RequestParam Long postIdx,
                                                         @RequestParam Long userStudyIdx,
                                                         @RequestBody PostSaveRequestDto requestDto) {
        try {
            return sendResponseHttpByJson(SUCCESS, "글 수정 성공",
                    postService.updatePost(postIdx, userStudyIdx, requestDto));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseApiMessage> deletePost(@RequestParam Long postIdx,
                                                         @RequestParam Long userStudyIdx) {
        try {
            postService.deletePost(postIdx, userStudyIdx);
            return sendResponseHttpByJson(SUCCESS, "글 수정 성공", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }
}
