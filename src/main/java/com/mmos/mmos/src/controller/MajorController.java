package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.major.MajorResponseDto;
import com.mmos.mmos.src.domain.dto.major.MajorSaveRequestDto;
import com.mmos.mmos.src.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.GET_MAJOR_EMPTY_RETURN;
import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/majors")
@RequiredArgsConstructor
public class MajorController extends BaseController {

    private final MajorService majorService;

    /**
     * 관리자 전용
     * Major 초기 데이터를 저장하는 API (완료)
     * @param collegeIdx (단과대 인덱스)
     * @param requestDto
     *          - majorIdx (학과 이름)
     */
    // 학과 전체 조회
    @ResponseBody
    @PostMapping("/save/{collegeIdx}")
    public ResponseEntity<ResponseApiMessage> saveMajor(@PathVariable Long collegeIdx, @RequestBody MajorSaveRequestDto requestDto) {
        MajorResponseDto responseDto = majorService.saveMajor(collegeIdx, requestDto);

        return sendResponseHttpByJson(SUCCESS, "Saved Majors", responseDto);

    }

    /**
     * 해당 단과대학에 존재하는 모든 학과를 조회하는 API (완료)
     * @param collegeIdx: 조회할 단과대학의 인덱스
     */
    @ResponseBody
    @GetMapping("get/{collegeIdx}")
    public  ResponseEntity<ResponseApiMessage> getMajors(@PathVariable Long collegeIdx){
        List<MajorResponseDto> majorResponseDtoList = majorService.getMajors(collegeIdx);

        if(majorResponseDtoList.isEmpty()){
            return sendResponseHttpByJson(GET_MAJOR_EMPTY_RETURN, "해당 단과대학에 학과가 존재하지 않습니다.", null);
        }
        return sendResponseHttpByJson(SUCCESS, "Got Majors", majorResponseDtoList);
    }
}
