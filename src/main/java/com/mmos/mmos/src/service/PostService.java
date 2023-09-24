package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.post.PostResponseDto;
import com.mmos.mmos.src.domain.dto.post.PostSaveRequestDto;
import com.mmos.mmos.src.domain.dto.post.PostUpdateRequestDto;
import com.mmos.mmos.src.domain.entity.Post;
import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.PostRepository;
import com.mmos.mmos.src.repository.StudyRepository;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;

    public User findUserByIdx(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. USER_INDEX=" + userIdx));
    }

    public Study findStudyByIdx(Long studyIdx) {
        return studyRepository.findById(studyIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. STUDY_INDEX=" + studyIdx));
    }

    public Post findPostByIdx(Long postIdx){
        return postRepository.findById(postIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다. POST_INDEX=" + postIdx));
    }

    public List<Post> findPostsByPostIsNotice(Boolean isNotice){
        return postRepository.findPostsByPostIsNotice(isNotice)
                .orElseThrow(() -> new IllegalArgumentException(" 공지글 == true, 홍보글 == false 다시 검색해 주세요. 현재 입력 =  " + isNotice));
    }

    @Transactional
    public PostResponseDto savePost(PostSaveRequestDto postSaveRequestDto, Long userIdx, Long studyIdx) {

        // User, Study 검색
        User user = findUserByIdx(userIdx);
        Study study = findStudyByIdx(studyIdx);

        // Post 생성/매핑
        Post post = new Post(postSaveRequestDto, user.getUserName(), study);
        study.addPost(post);
        postRepository.save(post);

        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long postIdx, PostUpdateRequestDto postUpdateRequestDto) {
        Post post = findPostByIdx(postIdx);

        // Post 내용, 이름 중복 검사
        if(post.getPostContents().equals(postUpdateRequestDto.getContents())) return null;
        if(post.getPostTitle().equals(postUpdateRequestDto.getTitle())) return null;

        // Post 업데이트
        post.update(postUpdateRequestDto);

        return new PostResponseDto(post);
    }

    // 게시글 단일 조회
    @Transactional
    public PostResponseDto getPost(Long postIdx){
        Post post = findPostByIdx(postIdx);

        return new PostResponseDto(post);
    }

    //게시글 전체 조회
    @Transactional
    public List<PostResponseDto> getPosts(Boolean isNotice) {
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        List<Post> posts = findPostsByPostIsNotice(isNotice);

        for (Post post : posts) {
            if (isNotice) {
                if (post.getPostIsNotice()) {
                    responseDtoList.add(new PostResponseDto(post));
                }
            } else {
                if (!post.getPostIsNotice()) {
                    responseDtoList.add(new PostResponseDto(post));
                }
            }
        }
        return responseDtoList;
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postIdx) {
        Post post = findPostByIdx(postIdx);
        postRepository.delete(post);
    }

}
