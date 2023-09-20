package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.user.UserNicknameUpdateDto;
import com.mmos.mmos.src.domain.dto.user.UserPwdUpdateDto;
import com.mmos.mmos.src.domain.dto.user.UserSaveRequestDto;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUser(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. USER_INDEX" + userIdx));
    }

    // 유저 생성
    @Transactional
    public User saveUser(UserSaveRequestDto requestDto) {
        User user = requestDto.toEntity();

        return userRepository.save(user);
    }

    // 비밀번호 변경
    @Transactional
    public void updatePwd(UserPwdUpdateDto userPwdUpdateDto, Long userIdx) {
        // 유저 정보 찾기
        User user = findUser(userIdx);
        // 유저 비밀번호 변경
        user.updatePwd(userPwdUpdateDto.getNewPwd());
    }

    // 닉네임 변경
    @Transactional
    public void updateNickname(UserNicknameUpdateDto userNicknameUpdateDto, Long userIdx) {

        // 유저 정보 찾기
        User user = findUser(userIdx);
        // 유저 비밀번호 변경
        user.updateNickname(userNicknameUpdateDto.getNewNickname());
    }

    // 프로필 이미지 변경
    @Transactional
    public void updatePfp(String pfp, Long userIdx) {
        // 유저 정보 찾기
        User user = findUser(userIdx);
        // 유저 프로필 이미지 변경
        user.updatePfp(pfp);
    }
}
