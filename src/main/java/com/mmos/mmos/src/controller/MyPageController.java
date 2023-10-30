package com.mmos.mmos.src.controller;

import com.mmos.mmos.src.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class MyPageController {

    private final UserService userService;

}
