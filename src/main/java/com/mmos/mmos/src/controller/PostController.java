package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.post.PostSaveRequestDto;
import com.mmos.mmos.src.domain.entity.Post;
import com.mmos.mmos.src.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController extends BaseController {

    private final PostService postService;

    @PostMapping("/{userIdx}/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> savePost(@RequestBody PostSaveRequestDto postSaveRequestDto, @PathVariable Long userIdx, @PathVariable Long studyIdx) {

        System.out.println("postSaveRequestDto.toString() = " + postSaveRequestDto.toString());

        Post post;
        // 글 생성

        post = postService.savePost(postSaveRequestDto, userIdx, studyIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE POST. POST_INDEX=" + post.getPost_index(), postSaveRequestDto);
    }
}
