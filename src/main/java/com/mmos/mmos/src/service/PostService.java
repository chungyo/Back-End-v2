package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.config.exception.NotAuthorizedAccessException;
import com.mmos.mmos.src.domain.dto.request.PostSaveRequestDto;
import com.mmos.mmos.src.domain.dto.response.study.NoticeSectionDto;
import com.mmos.mmos.src.domain.dto.response.study.PromotionSectionDto;
import com.mmos.mmos.src.domain.entity.Post;
import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserStudy;
import com.mmos.mmos.src.repository.PostRepository;
import com.mmos.mmos.src.repository.UserStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserStudyRepository userStudyRepository;

    public Post findPostByIdx(Long postIdx) throws BaseException {
        return postRepository.findById(postIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_POST));
    }

    public UserStudy findUserStudyByIdx(Long userStudyIdx) throws BaseException {
        return userStudyRepository.findById(userStudyIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USERSTUDY));
    }

    public List<Post> findPostsByPostIsNotice() throws BaseException {
        return postRepository.findPostsByPostIsNoticeIsFalse()
                .orElseThrow(() -> new EmptyEntityException(EMPTY_POST));
    }

    public List<Post> searchPromotion(String searchStr) throws BaseException {
        return postRepository.findPostsByPostTitleContainingIgnoreCaseOrPostContentsContainingIgnoreCase(searchStr, searchStr)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_POST));
    }

    @Transactional
    public Post savePost(Long userStudyIdx, PostSaveRequestDto postSaveRequestDto) throws BaseException {
        try {
            UserStudy userStudy = findUserStudyByIdx(userStudyIdx);
            if (!userStudy.getUserstudyMemberStatus().equals(1))
                throw new NotAuthorizedAccessException(NOT_AUTHORIZED);

            User user = userStudy.getUser();
            Study study = userStudy.getStudy();

            // Post 생성/매핑
            Post post = new Post(postSaveRequestDto, user, study);
            study.addPost(post);

            return postRepository.save(post);
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Post updatePost(Long postIdx, Long userStudyIdx, PostSaveRequestDto requestDto) throws BaseException {
        try {
            UserStudy userStudy = findUserStudyByIdx(userStudyIdx);
            if(!userStudy.getUserstudyMemberStatus().equals(1))
                throw new NotAuthorizedAccessException(NOT_AUTHORIZED);

            Post post = findPostByIdx(postIdx);

            boolean isEdit = false;
            if (requestDto.getPostTitle() != null) {
                post.updateTitle(requestDto.getPostTitle().toLowerCase());
                isEdit = true;
            } if(requestDto.getPostContents() != null) {
                post.updateContents(requestDto.getPostContents().toLowerCase());
                isEdit = true;
            } if(requestDto.getPostImage() != null) {
                post.updateImage(requestDto.getPostImage());
                isEdit = true;
            } if(isEdit) {
                post.updateWriter(userStudy.getUser());
                post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            }

            return post;
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 게시글 단일 조회
    @Transactional
    public Post getPost(Long postIdx) throws BaseException {
        try {
            return findPostByIdx(postIdx);
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 홍보 게시글 전체 조회
    @Transactional
    public Page<PromotionSectionDto> getPromotions(Pageable pageable) throws BaseException {
        try {
            List<PromotionSectionDto> responseDtoList = new ArrayList<>();
            List<Post> posts = findPostsByPostIsNotice();

            for (Post post : posts) {
                responseDtoList.add(new PromotionSectionDto(post));
            }

            return new PageImpl<>(responseDtoList, pageable, responseDtoList.size());
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 내 스터디 공지 게시글 조회
    @Transactional
    public Page<NoticeSectionDto> getNotices(UserStudy userStudy, Pageable pageable) throws BaseException {
        try {
            List<NoticeSectionDto> responseDtoList = new ArrayList<>();
            Study study = userStudy.getStudy();

            for (Post studyPost : study.getStudyPosts()) {
                responseDtoList.add(new NoticeSectionDto(studyPost));
            }

            return new PageImpl<>(responseDtoList, pageable, responseDtoList.size());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postIdx, Long userStudyIdx) throws BaseException {
        try {
            UserStudy userStudy = findUserStudyByIdx(userStudyIdx);
            if (!userStudy.getUserstudyMemberStatus().equals(1))
                throw new NotAuthorizedAccessException(NOT_AUTHORIZED);

            Post post = findPostByIdx(postIdx);
            postRepository.delete(post);
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Page<Post> searchPromotionByTitleAndContents(String searchStr, Pageable pageable) throws BaseException {
        try {
            List<Post> posts = searchPromotion(searchStr);
            return new PageImpl<>(posts, pageable, posts.size());
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
