package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.post.PostSaveRequestDto;
import com.mmos.mmos.src.domain.entity.Notice;
import com.mmos.mmos.src.domain.entity.Post;
import com.mmos.mmos.src.domain.entity.Promotion;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.PostRepository;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public User findUser(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. USER_INDEX=" + userIdx));
    }

    @Transactional
    public Post savePostByNotice(PostSaveRequestDto postSaveRequestDto, Notice notice, Long userIdx) {
        User user = findUser(userIdx);
        Post post = new Post(postSaveRequestDto, notice, user.getUser_name());

        // add Notice
        notice.addPost(post);

        return postRepository.save(post);
    }

    @Transactional
    public Post savePostByPromotion(PostSaveRequestDto postSaveRequestDto, Promotion promotion, Long userIdx) {
        User user = findUser(userIdx);
        Post post = new Post(postSaveRequestDto, promotion, user.getUser_name());

        // add Notice
        promotion.addPost(post);

        return postRepository.save(post);
    }
}
