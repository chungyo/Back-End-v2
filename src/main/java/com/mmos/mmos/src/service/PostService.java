package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.post.PostResponseDto;
import com.mmos.mmos.src.domain.dto.post.PostSaveRequestDto;
import com.mmos.mmos.src.domain.dto.post.PostUpdateRequestDto;
import com.mmos.mmos.src.domain.entity.*;
import com.mmos.mmos.src.repository.PostRepository;
import com.mmos.mmos.src.repository.StudyRepository;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;

    public User findUser(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. USER_INDEX=" + userIdx));
    }

    public Study findStudy(Long studyIdx) {
        return studyRepository.findById(studyIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. STUDY_INDEX=" + studyIdx));
    }

    public Post findPost(Long postIdx){
        return postRepository.findById(postIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다. POST_INDEX=" + postIdx));
    }

    @Transactional
    public PostResponseDto savePost(PostSaveRequestDto postSaveRequestDto, Long userIdx, Long studyIdx) {

        // User, Study 검색
        User user = findUser(userIdx);
        Study study = findStudy(studyIdx);

        // Post 생성/매핑
        Post post = new Post(postSaveRequestDto, user.getUserName(), study);
        study.addPost(post);
        postRepository.save(post);

        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long postIdx, PostUpdateRequestDto postUpdateRequestDto) {
        Post post = findPost(postIdx);

        // Post 내용, 이름 중복 검사
        if(post.getPostContents().equals(postUpdateRequestDto.getContents())) return null;
        if(post.getPostTitle().equals(postUpdateRequestDto.getTitle())) return null;

        // Post 업데이트
        post.update(postUpdateRequestDto);

        return new PostResponseDto(post);
    }
}
