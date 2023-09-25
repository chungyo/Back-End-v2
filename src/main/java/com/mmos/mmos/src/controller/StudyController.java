package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.study.StudyNameUpdateDto;
import com.mmos.mmos.src.domain.dto.study.StudyResponseDto;
import com.mmos.mmos.src.domain.dto.study.StudySaveRequestDto;
import com.mmos.mmos.src.domain.dto.user.UserResponseDto;
import com.mmos.mmos.src.domain.dto.userstudy.UserStudyResponseDto;
import com.mmos.mmos.src.service.StudyService;
import com.mmos.mmos.src.service.UserStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("/api/v1/studies")
@RequiredArgsConstructor
public class StudyController extends BaseController {
    private final StudyService studyService;
    private final UserStudyService userStudyService;

    /**
     * 스터디 생성하는 API (완료)
     * @param requestDto
     *          Integer memberLimit: 최대 멤버 설정
     *          String name: 스터디 방 이름
     * @param userIdx: 유저 인덱스
     */
    // Study 방 생성
    @ResponseBody
    @PostMapping("/save/{userIdx}")
    public ResponseEntity<ResponseApiMessage> saveStudy(@RequestBody StudySaveRequestDto requestDto, @PathVariable Long userIdx) {
        // Study 생성
        StudyResponseDto studyResponseDto = studyService.saveStudy(requestDto);

        // UserStudy 생성 + study -> UserStudy 매핑
        UserStudyResponseDto userStudyResponseDto = userStudyService.saveUserStudy(1, userIdx,  studyResponseDto.getIndex());

        return sendResponseHttpByJson(SUCCESS, "SAVE STUDY. STUDY_INDEX=" + studyResponseDto.getIndex(), studyResponseDto);
    }

    /**
     * 스터디에 참가 요청을 보낸 유저 리스트 조회 API (초대 및 참가 요청 완료 후 다시 테스트)
     * @param studyIdx: 스터디 인덱스
     */
    // Study 신청자 조회
    @ResponseBody
    @GetMapping("/applier/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> getStudyApplier(@PathVariable Long studyIdx) {
        // Study 지원자 리스트
        List<UserResponseDto> userResponseDtoList= studyService.getStudyAppliers(studyIdx);

        return sendResponseHttpByJson(SUCCESS, "GET APPLIERS.",userResponseDtoList);
    }

    /**
     * 스터디 멤버 리스트로 조회하는 API (완료)
     * @param studyIdx: 스터디 인덱스
     */
    // Study 멤버 조회
    @ResponseBody
    @GetMapping("/members/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> getStudyMembers(@PathVariable Long studyIdx) {
        // Study 멤버 리스트
        List<UserResponseDto> userResponseDtoList= studyService.getStudyMembers(studyIdx);

        return sendResponseHttpByJson(SUCCESS, "GET APPLIERS.",userResponseDtoList);
    }

    /**
     * 스터디 방 이름 변경하는 API (완료)
     * @param studyNameUpdateDto
     *          String newStudyName: 새 이름
     * @param studyIdx: 스터디 인덱스
     */
    // Study 이름 변경
    @ResponseBody
    @PatchMapping("/nameupdate/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> updateStudyName(@RequestBody StudyNameUpdateDto studyNameUpdateDto, @PathVariable Long studyIdx) {
        // null 검사
        if (studyNameUpdateDto.getNewStudyName() == null)
            return sendResponseHttpByJson(UPDATE_STUDY_EMPTY_NAME, "UPDATE STUDY_NAME FAIL. USER_IDX=" + studyIdx, null);

        // Study 이름 업데이트
        StudyResponseDto studyResponseDto = studyService.updateStudyName(studyIdx, studyNameUpdateDto.getNewStudyName());

        if(studyResponseDto == null)
            return sendResponseHttpByJson(UPDATE_STUDY_DUPLICATE_NAME, "UPDATE STUDY_NAME FAIL. DUPLICATE NAME", null);
         return sendResponseHttpByJson(SUCCESS, "UPDATE STUDY_NAME. STUDY_INDEX=" + studyIdx, studyResponseDto);
    }

    /**
     * 스터디가 종료되어 완수한 상태로 변경하는 API (완료)
     * @param studyIdx: 스터디 인덱스
     */
    // Study 완료 처리
    @ResponseBody
    @PatchMapping("/complete/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> updateStudyIsComplete(@PathVariable Long studyIdx){
        // Study 완료 업데이트
        StudyResponseDto studyResponseDto = studyService.updateStudyIsComplete(studyIdx);

        if(studyResponseDto == null)
            return sendResponseHttpByJson(UPDATE_STUDY_ALREADY_COMPLETE, "UPDATE STUDY_NAME FAIL. ALREADY COMPLETE", null);
        return sendResponseHttpByJson(SUCCESS, "UPDATE STUDY_COMPLETE. STUDY_INDEX=" + studyIdx, studyResponseDto);
    }

    /**
     * 스터디 맴버 추방 (수정 중)
     * @param userStudyIdx
     * @return
     */
    // Study 멤버 추방
    @ResponseBody
    @DeleteMapping("/{userStudyIdx}/remove")
    public ResponseEntity<ResponseApiMessage> removeUserStudy(@PathVariable Long userStudyIdx){
        // Study 완료 업데이트
        UserResponseDto userResponseDto = studyService.deleteUserFromStudy(userStudyIdx);

        return sendResponseHttpByJson(SUCCESS, "DELETE USER_STUDY_COMPLETE. USER_STUDY_INDEX=" + userStudyIdx, userResponseDto);
    }
}
