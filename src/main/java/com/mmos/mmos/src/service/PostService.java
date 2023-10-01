package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.post.PostResponseDto;
import com.mmos.mmos.src.domain.dto.post.PostSaveRequestDto;
import com.mmos.mmos.src.domain.dto.post.PostUpdateRequestDto;
import com.mmos.mmos.src.domain.entity.Post;
import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserStudy;
import com.mmos.mmos.src.repository.PostRepository;
import com.mmos.mmos.src.repository.StudyRepository;
import com.mmos.mmos.src.repository.UserRepository;
import com.mmos.mmos.src.repository.UserStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final UserStudyRepository userStudyRepository;

    public User findUserByIdx(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. USER_INDEX=" + userIdx));
    }

    public Study findStudyByIdx(Long studyIdx) {
        return studyRepository.findById(studyIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. STUDY_INDEX=" + studyIdx));
    }

    public Post findPostByIdx(Long postIdx) {
        return postRepository.findById(postIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다. POST_INDEX=" + postIdx));
    }

    public List<Post> findPostsByPostIsNotice(Boolean isNotice) {
        return postRepository.findPostsByPostIsNotice(isNotice)
                .orElseThrow(() -> new IllegalArgumentException(" 공지글 == true, 홍보글 == false 다시 검색해 주세요. 현재 입력 =  " + isNotice));
    }

    public List<Post> findPostsByStudy(Study study) {
        return postRepository.findPostsByPostIsNoticeAndStudy(true, study)
                .orElse(null);
    }

    public UserStudy findUserStudyByIdx(Long userStudyIdx) {
        return userStudyRepository.findById(userStudyIdx)
                .orElse(null);
    }

    @Transactional
    public PostResponseDto savePost(PostSaveRequestDto postSaveRequestDto, Long userIdx, Long studyIdx) {
        UserStudy userStudy = userStudyRepository.findUserStudyByStudy_StudyIndexAndUser_UserIndex(studyIdx, userIdx).orElse(null);
        if (userStudy == null)
            return new PostResponseDto(USERSTUDY_NOT_EXIST_USERSTUDY);
        if (userStudy.getUserstudyMemberStatus() > 2)
            return new PostResponseDto(POST_NOT_AUTHORIZED);

        // User, Study 검색
        User user = findUserByIdx(userIdx);
        Study study = findStudyByIdx(studyIdx);

        // Post 생성/매핑
        Post post = new Post(new PostSaveRequestDto(postSaveRequestDto.getPostTitle().toLowerCase(),
                                                    postSaveRequestDto.getPostContents().toLowerCase(),
                                                    postSaveRequestDto.getPostImage(),
                                                    postSaveRequestDto.getIsNotice()),
                                user,
                                study);
        study.addPost(post);
        postRepository.save(post);

        return new PostResponseDto(post, SUCCESS);
    }

    @Transactional
    public PostResponseDto updatePost(Long postIdx, PostUpdateRequestDto postUpdateRequestDto, Long userIdx) {
        User user = findUserByIdx(userIdx);
        Post post = findPostByIdx(postIdx);

        if (!post.getPostWriterIndex().equals(user.getUserIndex()))
            return new PostResponseDto(POST_NOT_AUTHORIZED);

        if (postUpdateRequestDto.getTitle() != null)
            post.updateTitle(postUpdateRequestDto.getTitle().toLowerCase());
        if (postUpdateRequestDto.getContents() != null)
            post.updateContents(postUpdateRequestDto.getContents().toLowerCase());
        if (postUpdateRequestDto.getImage() != null)
            post.updateImage(postUpdateRequestDto.getImage());

        return new PostResponseDto(post, SUCCESS);
    }

    // 게시글 단일 조회
    @Transactional
    public PostResponseDto getPost(Long postIdx) {
        Post post = findPostByIdx(postIdx);

        return new PostResponseDto(post, SUCCESS);
    }

    // 홍보 게시글 전체 조회
    @Transactional
    public Page<PostResponseDto> getPromotions(Boolean isNotice, Pageable pageable) {
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        List<Post> posts = findPostsByPostIsNotice(isNotice);
        for (Post post : posts) {
            if (!post.getPostIsNotice()) {
                responseDtoList.add(new PostResponseDto(post));
            }
        }

        return new PageImpl<>(responseDtoList, pageable, responseDtoList.size());
    }

    // 내 스터디 공지 게시글 조회
    @Transactional
    public Page<PostResponseDto> getNotices(Long userIdx, Long studyIdx, Pageable pageable) {
        Study study = findStudyByIdx(studyIdx);
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        if(study == null) {
//            responseDtoList.add(new PostResponseDto(USERSTUDY_NOT_EXIST_USERSTUDY));
//            return responseDtoList;
            return null;
        }
        List<Post> postList = findPostsByStudy(study);
        for (Post post : postList) {
            responseDtoList.add(new PostResponseDto(post));
        }

        return new PageImpl<>(responseDtoList, pageable, responseDtoList.size());
    }

    // 게시글 삭제
    @Transactional
    public Long deletePost(Long postIdx, Long userStudyIdx) {
        Post post = findPostByIdx(postIdx);

        UserStudy userStudy = findUserStudyByIdx(userStudyIdx);
        if (userStudy == null)
            return -1L; // USERSTUDY_NOT_EXIST_USERSTUDY
        if (userStudy.getUserstudyMemberStatus() > 2)
            return -2L; // POST_NOT_AUTHORIZED


        postRepository.delete(post);

        return postIdx;
    }

    @Transactional
    public Page<PostResponseDto> searchPromotionByTitleAndContents(String searchStr, Pageable pageable) {
        try {
            List<Post> postList = postRepository.findPostsByPostTitleContainingOrPostContentsContaining(searchStr.toUpperCase(), searchStr.toUpperCase()).orElse(null);
            List<PostResponseDto> responseDtoList = new ArrayList<>();
            for (Post post : postList) {
                responseDtoList.add(new PostResponseDto(post));
            }

            return new PageImpl<>(responseDtoList, pageable, responseDtoList.size());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
