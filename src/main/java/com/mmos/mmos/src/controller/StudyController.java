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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
     * 스터디에 참가 요청을 보낸 유저 리스트 조회 API (완료)
     * @param studyIdx: 스터디 인덱스
     */
    // Study 신청자 조회
    @ResponseBody
    @GetMapping("/applier/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> getStudyApplier(@PathVariable Long studyIdx, @PageableDefault(page = 0, size = 10, sort = "userIndex", direction = Sort.Direction.ASC) Pageable pageable) {
        // Study 지원자 리스트
        Page<UserResponseDto> userResponseDtoList= studyService.getStudyAppliersOrInvitee(studyIdx, 5, pageable);

        return sendResponseHttpByJson(SUCCESS, "GET APPLIERS.", userResponseDtoList);
    }

    /**
     * 스터디가 초대 요청을 보낸 유저 리스트 조회 API (완료)
     * @param studyIdx: 스터디 인덱스
     */
    // Study 신청자 조회
    @ResponseBody
    @GetMapping("/invitee/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> getStudyInvitee(@PathVariable Long studyIdx, @PageableDefault(page = 0, size = 10, sort = "userIndex", direction = Sort.Direction.ASC) Pageable pageable) {
        // Study 지원자 리스트
        Page<UserResponseDto> userResponseDtoList= studyService.getStudyAppliersOrInvitee(studyIdx, 4, pageable);

        return sendResponseHttpByJson(SUCCESS, "GET APPLIERS.", userResponseDtoList);
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
     * 인원이 가장 많은 스터디 TOP 3를 구하는 API (완료)
     */
    // 인원이 가장 많은 스터디 랭킹
    @ResponseBody
    @GetMapping("/rank/people")
    public ResponseEntity<ResponseApiMessage> getPopularStudy() {
        List<StudyResponseDto> responseDtoList = studyService.getPopularStudy();

        return sendResponseHttpByJson(SUCCESS, "인원이 가장 많은 스터디 조회 완료", responseDtoList);
    }

    /**
     * 한 달 평균 공부 시간이 가장 많은 스터디 랭킹 (완료)
     */
    // 금주의 평균 공부 시간이 많은 스터디 랭킹
    @ResponseBody
    @GetMapping("/rank/time")
    public ResponseEntity<ResponseApiMessage> getHardestStudy() {
        List<StudyResponseDto> responseDtoList = studyService.getHardestStudy();

        return sendResponseHttpByJson(SUCCESS, "평균 공부 시간이 가장 많은 스터디 랭킹", responseDtoList);
    }

    /**
     * 한 달이 지나면 모든 스터디의 평균 공부 시간이 0으로 초기화 되는 API (완료)
     */
    @ResponseBody
    @PatchMapping("/reset")
    public ResponseEntity<ResponseApiMessage> resetAvgStudyTime() {
        studyService.resetAvgStudyTime();

        return sendResponseHttpByJson(SUCCESS, "평균 공부 시간이 가장 많은 스터디 랭킹", null);
    }

    /**
     * 스터디 조회 API (완료)
     * @param studyIdx: 조회하려는 스터디의 인덱스
     */
    @ResponseBody
    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getStudy(Long studyIdx) {
        StudyResponseDto responseDto = studyService.getStudy(studyIdx);

        if(responseDto.getStatus() == EMPTY_STUDY)
            return sendResponseHttpByJson(EMPTY_STUDY, "존재하지 않는 스터디입니다.", null);
        return sendResponseHttpByJson(SUCCESS, "스터디 조회 성공", responseDto);
    }

    /**
     * 스터디 목록 조회 API (완료)
     * @param pageable: 페이징 기본 파라미터
     */
    @ResponseBody
    @GetMapping("/all")
    public ResponseEntity<ResponseApiMessage> getStudies(@PageableDefault(page = 0, size = 5, sort = "studyIndex", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<StudyResponseDto> page = studyService.getStudies(pageable);

        return sendResponseHttpByJson(SUCCESS, "전체 스터디 조회 완료", page);
    }
}
