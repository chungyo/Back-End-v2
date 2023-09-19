package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.post.PostSaveRequestDto;
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
    public Study findStudy(Long studyIdx){
        return studyRepository.findById(studyIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. STUDY_INDEX=" + studyIdx));
    }
    @Transactional
    public Post savePost(PostSaveRequestDto postSaveRequestDto, Long userIdx, Long studyIdx) {
        User user = findUser(userIdx);
        Study study = findStudy(studyIdx);
        Post post = new Post(postSaveRequestDto, user.getUser_name(), study);
        System.out.println("Post =" + post.toString());
        study.addPost(post);
        return postRepository.save(post);
    }
}