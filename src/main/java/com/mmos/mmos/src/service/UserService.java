package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.tier.TierResponseDto;
import com.mmos.mmos.src.domain.dto.user.UserNicknameUpdateDto;
import com.mmos.mmos.src.domain.dto.user.UserPwdUpdateDto;
import com.mmos.mmos.src.domain.dto.user.UserSaveRequestDto;
import com.mmos.mmos.src.domain.entity.Tier;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.TierRepository;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TierRepository tierRepository;

    public User findUser(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. USER_INDEX" + userIdx));
    }

    public Tier findTier(Long tierIdx) {
        return tierRepository.findById(tierIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 티어입니다. TIER_INDEX=" + tierIdx));
    }

    // 유저 생성
    @Transactional
    public User saveUser(UserSaveRequestDto requestDto) {
        Tier tier = findTier(1L);
        User user = requestDto.toEntity(tier);

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

    // 티어 조회
    @Transactional
    public TierResponseDto updateTier(Long userIdx) {
        // 유저 정보 찾기
        User user = findUser(userIdx);

        return new TierResponseDto(user.getTier().getTier_index(), user.getTier().getTier_name(), user.getTier().getTier_image(), user.getTier().getTier_top_ratio());
    }
}
