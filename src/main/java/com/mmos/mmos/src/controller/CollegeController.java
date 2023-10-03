package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.college.CollegeResponseDto;
import com.mmos.mmos.src.domain.dto.university.UniversitySaveRequestDto;
import com.mmos.mmos.src.service.CollegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("api/v1/colleges")
@RequiredArgsConstructor
public class CollegeController extends BaseController {

    private final CollegeService collegeService;

    /**
     * 관리자 전용
     * College 초기 데이터를 저장하는 API (완료)
     * @param universityIdx: 대학 인덱스
     * @param requestDto
     *          - collegeName: 단과대 이름
     */
    @ResponseBody
    @PostMapping("/{universityIdx}")
    public ResponseEntity<ResponseApiMessage> saveCollege(@PathVariable Long universityIdx, @RequestBody UniversitySaveRequestDto requestDto) {
        CollegeResponseDto responseDto = collegeService.saveCollege(universityIdx, requestDto);

        if(responseDto.getStatus().equals(INVALID_UNIVERSITY))
            return sendResponseHttpByJson(INVALID_UNIVERSITY, "대학교가 존재하지 않습니다. UniversityIdx = " + universityIdx,null);
        return sendResponseHttpByJson(SUCCESS, "Saved University", responseDto);
    }

    /**
     * 해당 대학교에 존재하는 모든 단과대학을 조회하는 API (완료)
     * @param universityIdx: 조회할 대학교의 인덱스
     */
    @ResponseBody
    @GetMapping("get/{universityIdx}")
    public  ResponseEntity<ResponseApiMessage> getColleges(@PathVariable Long universityIdx){
        List<CollegeResponseDto> collegeResponseDtoList = collegeService.getColleges(universityIdx);

        if(collegeResponseDtoList.isEmpty()){
            return sendResponseHttpByJson(GET_COLLEGE_EMPTY_RETURN, "해당 대학교에 단과대학이 존재하지 않습니다.", null);
        }
        if(collegeResponseDtoList.get(1).getStatus().equals(INVALID_UNIVERSITY)){
            return sendResponseHttpByJson(GET_COLLEGE_EMPTY_RETURN, "대학교가 존재하지 않습니다. UniversityIdx = "+universityIdx, null);
        }
        return sendResponseHttpByJson(SUCCESS, "Got Colleges", collegeResponseDtoList);
    }
}
