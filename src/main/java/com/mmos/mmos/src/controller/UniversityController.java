package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.entity.University;
import com.mmos.mmos.src.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/universities")
@RequiredArgsConstructor
public class UniversityController extends BaseController{
    private final UniversityService universityService;

    @ResponseBody
    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getUniversities(@RequestParam(required = false) Long universityIdx){
        System.out.println("universityIdx = " + universityIdx);
        if(universityIdx == null){
            List<University> universities = universityService.findUniversities();

            return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "GET UNIVERSITIES.", universities);
        }
        else {
            University university = universityService.findUniversity(universityIdx);

            return sendResponseHttpByJson(HttpResponseStatus.SUCCESS, "GET UNIVERSITY. UNIVERSITY_INDEX = " + universityIdx, university);
        }
    }
}
