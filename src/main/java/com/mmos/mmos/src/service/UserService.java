package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.study.StudyResponseDto;
import com.mmos.mmos.src.domain.dto.user.UserNicknameUpdateDto;
import com.mmos.mmos.src.domain.dto.user.UserPwdUpdateDto;
import com.mmos.mmos.src.domain.dto.user.UserResponseDto;
import com.mmos.mmos.src.domain.dto.user.UserSaveRequestDto;
import com.mmos.mmos.src.domain.entity.*;
import com.mmos.mmos.src.repository.MajorRepository;
import com.mmos.mmos.src.repository.StudyRepository;
import com.mmos.mmos.src.repository.UniversityRepository;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;
    public User findUserByIdx(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. USER_INDEX" + userIdx));
    }

    public University findUniversityByIdx(Long universityIdx) {
        return universityRepository.findById(universityIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대학입니다 UNIVERSITY_INDEX=" + universityIdx));
    }


    // GET: 학과
//    public Major findMajorByIdxAndCollege(Long majorIdx, String majorCollege) {
//        return majorRepository.findByMajorIndexAndMajorCollege(majorIdx, majorCollege)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다. UNIVERSITY_INDEX=" + majorIdx));
//    }

    public Major findMajorByIdx(Long majorIdx) {
        return majorRepository.findMajorByMajorIndex(majorIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다. MAJOR_INDEX=" + majorIdx));
    }

    public List<StudyResponseDto> getStudies(Long userIdx){

        User user = findUserByIdx(userIdx);

        // 유저가 가진 유저스터디 리스트
        List<UserStudy> userStudyList = user.getUserUserstudies();

        // 유저가 가진 스터디 Dto 리스트 생성
        List<StudyResponseDto> studyResponseDtoList = new ArrayList<>();
        for (UserStudy userStudy : userStudyList){
            studyResponseDtoList.add(new StudyResponseDto(userStudy.getStudy()));
        }

        return studyResponseDtoList;
    }

    // 유저 생성
    @Transactional
    public UserResponseDto saveUser(UserSaveRequestDto requestDto) {
        Major major = findMajorByIdx(requestDto.getMajorIdx());

        // Validation: 생성하려는 유저의 정보로 이미 가입된 회원이 있는지 확인 (학교 & 학번, 이메일, 닉네임)
        if(userRepository.findUserByUserEmail(requestDto.getEmail()).isPresent())
            return null;

        if(userRepository.findUserByUserNickname(requestDto.getNickname()).isPresent()){
            System.out.println("중복된 닉네임");
            return null;
        }

        User user = new User(requestDto, major);


        userRepository.save(user);

        // 대학, 전공 역 FK 설정
        major.addUser(user);

        return new UserResponseDto(user);
    }

    // 비밀번호 변경
    @Transactional
    public UserResponseDto updatePwd(UserPwdUpdateDto userPwdUpdateDto, Long userIdx) {
        // 유저 정보 찾기
        User user = findUserByIdx(userIdx);

        // 사용자가 입력한 이전 비밀번호와 DB에 저장된 비밀번호와 같은지 확인
        if(user.getUserPassword().equals(userPwdUpdateDto.getPrevPwd()))
            return null;

        // 유저 비밀번호 변경
        user.updatePwd(userPwdUpdateDto.getNewPwd());

        return new UserResponseDto(user);
    }

    // 닉네임 변경: 이전 닉네임과 같은지 확인하지 않은 이유 -> 이전 닉네임과 비교하려면 DB와 접촉을 한번 더 해야하므로 Regex만 거치면 그냥 받는대로 UPDATE
    @Transactional
    public UserResponseDto updateNickname(UserNicknameUpdateDto userNicknameUpdateDto, Long userIdx) {
        // 유저 정보 찾기
        User user = findUserByIdx(userIdx);

        if(user.getUserNickname().equals(userNicknameUpdateDto.getNewNickname()))
            return null;

        // 유저 닉네임 변경
        user.updateNickname(userNicknameUpdateDto.getNewNickname());

        return new UserResponseDto(user);
    }
}
