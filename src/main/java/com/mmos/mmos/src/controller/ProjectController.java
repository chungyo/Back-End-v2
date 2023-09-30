package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.project.ProjectResponseDto;
import com.mmos.mmos.src.domain.dto.project.ProjectSaveRequestDto;
import com.mmos.mmos.src.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController extends BaseController{
    private final ProjectService projectService;

    /**
     * 새로운 프로젝트 생성하는 API
     * @param userIdx: 프로젝트 생성하려는 사용자 인덱스
     * @param projectSaveRequestDto
     *          LocalDate startTime: 프로젝트 시작 날
     *          LocalDate endTime: 프로젝트 종료 날
     *          String name: 프로젝트 이름
     */
    @PostMapping("/save/{userIdx}")
    @ResponseBody
    public ResponseEntity<ResponseApiMessage> saveProject(@PathVariable Long userIdx, @RequestBody ProjectSaveRequestDto projectSaveRequestDto){
        // null 검사
        if(projectSaveRequestDto.getName()==null) {
            return sendResponseHttpByJson(HttpResponseStatus.POST_PROJECT_EMPTY_NAME,"EMPTY_NAME.",null);
        }

        ProjectResponseDto projectResponseDto = projectService.saveProject(userIdx,projectSaveRequestDto);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE PROJECT. PROJECT IDX=" + projectResponseDto.getProjectIndex(), projectResponseDto);
    }

    @PatchMapping("/updateName/{userIdx}")
    @ResponseBody
    public ResponseEntity<ResponseApiMessage> updateProjectName(@PathVariable Long userIdx,@PathVariable Long projectIdx, @RequestBody ProjectSaveRequestDto projectSaveRequestDto){
        // null 검사
        if(projectSaveRequestDto.getName()==null) {
            return sendResponseHttpByJson(HttpResponseStatus.POST_PROJECT_EMPTY_NAME,"EMPTY_NAME.",null);
        }

        ProjectResponseDto projectResponseDto = projectService.saveProject(userIdx,projectSaveRequestDto);

        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "SAVE PROJECT. PROJECT IDX=" + projectResponseDto.getProjectIndex(), projectResponseDto);
    }
}
