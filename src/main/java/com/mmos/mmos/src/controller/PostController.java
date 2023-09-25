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

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController extends BaseController {

    private final PostService postService;

    /**
     * 게시물 등록 (수정 중 - 스터디 리더진인지 확인 안함)
     * @param postSaveRequestDto
     *          - String postTitle: 게시물 제목
     *          - String postContents: 게시물 내용
     *          - String postImage: 게시물 이미지 주소
     *          - Boolean isNotice: 공지사항 여부
     * @param userIdx: 유저 인덱스
     * @param studyIdx: 스터디 인덱스
     * @return
     */
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

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE POST. POST_INDEX=" + postResponseDto.getIdx(), postSaveRequestDto);
    }

    /**
     * 게시물 수정하는 API (수정 중 - 로직 잘못됨)
     * @param postUpdateRequestDto
     *          - String title: 수정할 게시물 제목
     *          - String contents: 수정할 게시물 내용
     *          - String image: 수정할 게시물 사진
     * @param postIdx: 게시물 인덱스
     */
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

    /**
     * 홍보글 전체 조회하는 API (완료)
     */
    // 홍보글 조회(전체)
    @GetMapping("/promotion/all")
    public ResponseEntity<ResponseApiMessage> getPromotions() {
        List<PostResponseDto> responseDtoList = postService.getPosts(false);

        return sendResponseHttpByJson(SUCCESS, "GET PROMOTIONS.", responseDtoList);
    }

    /**
     * 수정 중 - 로직 잘못 됨. 같은 스터디 내 인원들만 조회가 가능해야 함.
     * @return
     */
    // 공지글 조회(전체)
    @GetMapping("/notice/all")
    public ResponseEntity<ResponseApiMessage> getNotices() {
        List<PostResponseDto> responseDtoList = postService.getPosts(true);

        return sendResponseHttpByJson(SUCCESS, "GET NOTICES.", responseDtoList);
    }

    /**
     * 게시물 인덱스로 하나만 조회하는 API (완료)
     * @param postIdx: 게시물 인덱스
     */
    // 게시글 조회(단일)
    @ResponseBody
    @GetMapping("/{postIdx}")
    public ResponseEntity<ResponseApiMessage> getPost(@PathVariable Long postIdx) {
        PostResponseDto responseDto = postService.getPost(postIdx);

        return sendResponseHttpByJson(SUCCESS, "GET POST. POST_INDEX = " + postIdx, responseDto);
    }

    /**
     * 수정 중 - 삭제 권한은 리더인지 확인 X, 삭제 시, 리턴을 삭제하려는 인덱스로 하기로 함.
     * @param postIdx
     * @return
     */
    @ResponseBody
    @DeleteMapping("/{postIdx}")
    public ResponseEntity<ResponseApiMessage> deletePost(@PathVariable Long postIdx) {
        postService.deletePost(postIdx);
        return sendResponseHttpByJson(SUCCESS, "DELETE POST. POST_INDEX = " + postIdx, null);
    }

}
