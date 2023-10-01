package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.post.PostResponseDto;
import com.mmos.mmos.src.domain.dto.post.PostSaveRequestDto;
import com.mmos.mmos.src.domain.dto.post.PostSearchRequestDto;
import com.mmos.mmos.src.domain.dto.post.PostUpdateRequestDto;
import com.mmos.mmos.src.service.PostService;
import com.mmos.mmos.src.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController extends BaseController {

    private final PostService postService;
    private final UserService userService;

    /**
     * 게시물 등록 (완료)
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
            return sendResponseHttpByJson(POST_POST_EMPTY_TITLE,"EMPTY POST_TITLE.", null);
        }
        if(postSaveRequestDto.getPostContents() == null) {
            return sendResponseHttpByJson(POST_POST_EMPTY_CONTENTS, "EMPTY POST_CONTENTS.", null);
        }

        // 글 생성
        PostResponseDto postResponseDto = postService.savePost(postSaveRequestDto, userIdx, studyIdx);

        if(postResponseDto.getStatus().equals(POST_NOT_AUTHORIZED))
            return sendResponseHttpByJson(POST_NOT_AUTHORIZED, "권한이 없습니다.", null);
        else if(postResponseDto.getStatus().equals(USERSTUDY_NOT_EXIST_USERSTUDY))
            return sendResponseHttpByJson(USERSTUDY_NOT_EXIST_USERSTUDY, "참여 중인 스터디가 아닙니다.", null);
        return sendResponseHttpByJson(SUCCESS, "SAVE POST. POST_INDEX=" + postResponseDto.getIdx(), postSaveRequestDto);
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
    public ResponseEntity<ResponseApiMessage> updatePost(@RequestBody PostUpdateRequestDto postUpdateRequestDto, @PathVariable Long postIdx, @PathVariable Long userIdx) {
        // 글 업데이트
        PostResponseDto postResponseDto = postService.updatePost(postIdx, postUpdateRequestDto, userIdx);

        if(postResponseDto.getStatus().equals(POST_NOT_AUTHORIZED))
            return sendResponseHttpByJson(POST_NOT_AUTHORIZED, "권한이 없습니다.", null);
        return sendResponseHttpByJson(SUCCESS, "UPDATE POST. POST_INDEX=" + postIdx, postResponseDto);
    }

    /**
     * 홍보글 전체 조회하는 API (완료)
     * @param pageable: 페이징 기본 파라미터
     */
    // 홍보글 조회(전체)
    @GetMapping("/promotion/all")
    public ResponseEntity<ResponseApiMessage> getPromotions(@PageableDefault(page = 0, size = 5, sort = "postIndex", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PostResponseDto> responseDtoList = postService.getPromotions(false, pageable);

        return sendResponseHttpByJson(SUCCESS, "GET PROMOTIONS.", responseDtoList);
    }

    /**
     * 공지글 전체 조회하는 API (완료)
     * @param userIdx: 내 userIdx
     * @param studyIdx: 내 스터디 studyIdx
     * @param pageable: 페이징 기본 파라미터
     */
    // 공지글 조회(전체)
    @GetMapping("/notice/all/{userIdx}/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> getNotices(@PathVariable Long userIdx, @PathVariable Long studyIdx, @PageableDefault(page = 0, size = 5, sort = "postIndex", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PostResponseDto> responseDtoList = postService.getNotices(userIdx, studyIdx, pageable);

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
     * 운영진 이상의 권한을 가진 멤버가 게시물 삭제하는 API
     * @param postIdx: 삭제하려는 postIdx
     * @Param userStudyIdx: 운영진 userStudyIdx
     */
    @ResponseBody
    @DeleteMapping("/{postIdx}/{userStudyIdx}")
    public ResponseEntity<ResponseApiMessage> deletePost(@PathVariable Long postIdx, @PathVariable Long userStudyIdx) {
        Long idx = postService.deletePost(postIdx, userStudyIdx);

        if(idx == -1L)
            return sendResponseHttpByJson(USERSTUDY_NOT_EXIST_USERSTUDY, "참여 중인 스터디가 아닙니다.", null);
        if(idx == -2L)
            return sendResponseHttpByJson(POST_NOT_AUTHORIZED, "권한이 없습니다." + idx, null);
        return sendResponseHttpByJson(SUCCESS, "DELETE POST. POST_INDEX = " + idx, idx);

    }

    // 홍보 글 검색
    @ResponseBody
    @GetMapping("/find")
    public ResponseEntity<ResponseApiMessage> searchPromotionByTitle(@RequestBody PostSearchRequestDto requestDto, @PageableDefault(page = 0, size = 5, sort = "postIndex", direction = Sort.Direction.ASC) Pageable pageable) {
        System.out.println(requestDto.getSearchStr());
        Page<PostResponseDto> page = postService.searchPromotionByTitleAndContents(requestDto.getSearchStr(), pageable);

        return sendResponseHttpByJson(SUCCESS, "GET POST.", page);
    }
}
