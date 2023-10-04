package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.study.StudyResponseDto;
import com.mmos.mmos.src.domain.dto.study.StudySaveRequestDto;
import com.mmos.mmos.src.domain.dto.user.UserResponseDto;
import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserStudy;
import com.mmos.mmos.src.repository.StudyRepository;
import com.mmos.mmos.src.repository.UserStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.EMPTY_STUDY;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final UserStudyRepository userStudyRepository;

    // 스터디 생성
    @Transactional
    public StudyResponseDto saveStudy(StudySaveRequestDto requestDto){
        Study study = new Study(requestDto);

        // 스터디 생성
        studyRepository.save(study);
        return new StudyResponseDto(study);
    }

    public Study findStudy(Long studyIdx){
        return studyRepository.findById(studyIdx)
                .orElse(null);
    }

    public UserStudy findUserStudy(Long userStudyIdx){
        return userStudyRepository.findById(userStudyIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. STUDY_INDEX" + userStudyIdx));
    }

    public List<Study> findPopularStudy() {
        return studyRepository.findTop3ByStudyIsCompleteAndAndStudyIsVisibleOrderByStudyMemberNumDesc(false, true)
                .orElse(null);
    }

    public List<Study> findHardestStudy() {
        return studyRepository.findTop3ByStudyIsCompleteAndStudyIsVisibleOrderByStudyAverageStudyTimeDesc(false, true)
                .orElse(null);
    }
    
    // 스터디 이름 업데이트
    @Transactional
    public StudyResponseDto updateStudyName(Long studyIdx, String newName) {
        Study study = findStudy(studyIdx);
        
        // 이름 중복 검사
        if(study.getStudyName().equals(newName)) return null;

        study.updateStudyName(newName);
        return new StudyResponseDto(study);
    }

    @Transactional
    public Page<UserResponseDto> getStudyAppliersOrInvitee(Long studyIdx, Integer status, Pageable pageable){
        Study study = findStudy(studyIdx);

        // UserStudy list에서 지원자 선별
        List<UserResponseDto> appliersDto = new ArrayList<>();
        for (UserStudy userStudy : study.getStudyUserstudies()){
            if(userStudy.getUserstudyMemberStatus().equals(status)) {
                appliersDto.add(new UserResponseDto(userStudy.getUser()));
            }
        }

        return new PageImpl<>(appliersDto, pageable, appliersDto.size());
    }

    @Transactional
    public List<UserResponseDto> getStudyMembers(Long studyIdx){
        Study study = findStudy(studyIdx);

        // UserStudy list에서 멤버 선별
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        for (UserStudy userStudy : study.getStudyUserstudies()){
            if(userStudy.getUserstudyMemberStatus() < 4){
                userResponseDtoList.add(new UserResponseDto(userStudy.getUser()));
            }
        }

        return userResponseDtoList;
    }

    // 스터디 완수 업데이트
    @Transactional
    public StudyResponseDto updateStudyIsComplete(Long studyIdx){
        Study study = findStudy(studyIdx);

        // 이미 완수된 경우
        if(study.getStudyIsComplete()) return null;
        
        // Study 완료 처리
        study.updateStudyIsComplete();

        return new StudyResponseDto(study);
    }

    @Transactional
    public UserResponseDto deleteUserFromStudy(Long userStudyIdx){

        UserStudy userStudy = findUserStudy(userStudyIdx);
        User user = userStudy.getUser();

        // userStudy 삭제
        userStudyRepository.deleteById(userStudyIdx);

        // 삭제된 유저 정보 Dto 반환
        return new UserResponseDto(user);
    }


    @Transactional
    public List<StudyResponseDto> getPopularStudy() {
        List<StudyResponseDto> responseDtoList = new ArrayList<>();
        List<Study> studyList = findPopularStudy();

        for (Study study : studyList) {
            responseDtoList.add(new StudyResponseDto(study));
        }

        return responseDtoList;
    }

    @Transactional
    public List<StudyResponseDto> getHardestStudy() {
        List<StudyResponseDto> responseDtoList = new ArrayList<>();
        List<Study> studyList = findHardestStudy();

        for (Study study : studyList) {
            if(study.getStudyAverageStudyTime() == 0L)
                continue;
            responseDtoList.add(new StudyResponseDto(study));
        }

        return responseDtoList;
    }

    @Transactional
    public Page<StudyResponseDto> getStudies(Pageable pageable) {
        List<StudyResponseDto> responseDtoList = new ArrayList<>();
        List<Study> studyList = studyRepository.findAll();

        for (Study study : studyList) {
            responseDtoList.add(new StudyResponseDto(study));
        }

        return new PageImpl<>(responseDtoList, pageable, responseDtoList.size());
    }

    @Transactional
    public StudyResponseDto getStudy(Long studyIdx) {
        Study study = findStudy(studyIdx);

        if(study == null)
            return new StudyResponseDto(EMPTY_STUDY);

        return new StudyResponseDto(study);
    }

    @Transactional
    public void resetAvgStudyTime() {
        List<Study> studyList = studyRepository.findAll();

        for (Study study : studyList) {
            study.resetAvgStudyTime();
        }
    }
}
