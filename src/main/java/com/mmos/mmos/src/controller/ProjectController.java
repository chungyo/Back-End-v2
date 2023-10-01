package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.project.ProjectNameUpdateDto;
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

    /**
     * 프로젝트의 이름을 바꾸는 API(완료)
     * @param userIdx : 이름을 바꾸려는 프로젝트를 소유한 유저의 인덱스
     * @param projectIdx : 이름을 바꾸려는 프로젝트의 인덱스
     * @param projectNameUpdateDto :
     *              String newName: 변경 후 프로젝트의 이름
     */
    @PatchMapping("/updateName/{userIdx}/{projectIdx}")
    @ResponseBody
    public ResponseEntity<ResponseApiMessage> updateProjectName(@PathVariable Long userIdx,@PathVariable Long projectIdx, @RequestBody ProjectNameUpdateDto projectNameUpdateDto){
        // null 검사
        if(projectNameUpdateDto.getNewName()==null) {
            return sendResponseHttpByJson(HttpResponseStatus.POST_PROJECT_EMPTY_NAME,"EMPTY_NAME.",null);
        }

        ProjectResponseDto projectResponseDto = projectService.updateProjectName(userIdx,projectIdx,projectNameUpdateDto);

        if(projectResponseDto.getStatus().equals(HttpResponseStatus.UPDATE_PROJECT_NOT_OWNER)){
            return sendResponseHttpByJson(HttpResponseStatus.UPDATE_PROJECT_NOT_OWNER,"프로젝트를 소유한 유저가 아닙니다.", null);
        }
        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "UPDATE PROJECT NAME. PROJECT IDX=" + projectIdx, projectResponseDto);
    }


    /**
     * 프로젝트를 삭제하는 API (완료)
     * @param userIdx : 삭제하려는 프로젝트를 소유한 유저의 인덱스
     * @param projectIdx : 삭제하려는 프로젝트의 인덱스
     * @return deleteProjectIdx : 삭제 완료한 프로젝트의 인덱스 (성공시 위의 projectIdx와 동일한 값)
     */
    @DeleteMapping("/delete/{userIdx}/{projectIdx}")
    @ResponseBody
    public ResponseEntity<ResponseApiMessage> deleteProject(@PathVariable Long userIdx, @PathVariable Long projectIdx){

        Long deleteProjectIdx = projectService.deleteProject(userIdx,projectIdx);
        if(deleteProjectIdx == null){
            return sendResponseHttpByJson(HttpResponseStatus.UPDATE_PROJECT_NOT_OWNER,"프로젝트를 소유한 유저가 아닙니다.", null);
        }
        return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "DELETE PROJECT. PROJECT IDX= " + deleteProjectIdx, null);
    }
}
