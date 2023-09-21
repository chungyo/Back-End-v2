package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.post.PostSaveRequestDto;
import com.mmos.mmos.src.domain.dto.post.PostUpdateRequestDto;
import com.mmos.mmos.src.domain.entity.Post;
import com.mmos.mmos.src.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController extends BaseController {

    private final PostService postService;

    @PostMapping("/{userIdx}/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> savePost(@RequestBody PostSaveRequestDto postSaveRequestDto, @PathVariable Long userIdx, @PathVariable Long studyIdx) {
       // 글 생성 시 제목, 내용 null 검사
        if(postSaveRequestDto.getPostTitle() == null) {
            return sendResponseHttpByJson(POST_POST_EMPTY_TITLE,"EMPTY POST_TITLE.", postSaveRequestDto);
        }
        if(postSaveRequestDto.getPostContents() == null) {
            return sendResponseHttpByJson(POST_POST_EMPTY_CONTENTS, "EMPTY POST_CONTENTS.", postSaveRequestDto);
        }

        // 글 생성
        Post post = postService.savePost(postSaveRequestDto, userIdx, studyIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE POST. POST_INDEX=" + post.getPostIndex(), postSaveRequestDto);
    }

    @ResponseBody
    @PatchMapping("/{userIdx}/{postIdx}")
    public ResponseEntity<ResponseApiMessage> updatePost(@RequestBody PostUpdateRequestDto postUpdateRequestDto, @PathVariable Long userIdx, @PathVariable Long postIdx) {

        System.out.println("postUpdateRequestDto.getPostTitle() = " + postUpdateRequestDto.getPost_title());
        // 글 업데이트
        postService.updatePost(postIdx, postUpdateRequestDto);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "UPDATE POST. POST_INDEX=" + postIdx, postUpdateRequestDto);
    }

}
