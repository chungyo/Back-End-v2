package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.post.PostResponseDto;
import com.mmos.mmos.src.domain.dto.post.PostSaveRequestDto;
import com.mmos.mmos.src.domain.dto.post.PostUpdateRequestDto;
import com.mmos.mmos.src.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mmos.mmos.config.HttpResponseStatus.POST_POST_EMPTY_CONTENTS;
import static com.mmos.mmos.config.HttpResponseStatus.POST_POST_EMPTY_TITLE;

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
        PostResponseDto postResponseDto = postService.savePost(postSaveRequestDto, userIdx, studyIdx);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE POST. POST_INDEX=" + postResponseDto.getIndex(), postSaveRequestDto);
    }

    @ResponseBody
    @PatchMapping("/{userIdx}/{postIdx}")
    public ResponseEntity<ResponseApiMessage> updatePost(@RequestBody PostUpdateRequestDto postUpdateRequestDto, @PathVariable Long postIdx) {
        // 글 업데이트 시 제목, 내용 null 검사
        if(postUpdateRequestDto.getTitle() == null) {
            return sendResponseHttpByJson(POST_POST_EMPTY_TITLE,"EMPTY POST_TITLE.", postUpdateRequestDto);
        }
        if(postUpdateRequestDto.getContents() == null) {
            return sendResponseHttpByJson(POST_POST_EMPTY_CONTENTS, "EMPTY POST_CONTENTS.", postUpdateRequestDto);
        }

        // 글 업데이트
        PostResponseDto postResponseDto = postService.updatePost(postIdx, postUpdateRequestDto);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "UPDATE POST. POST_INDEX=" + postIdx, postResponseDto);
    }

}
