package com.example.myapp.Membership.controller;

import com.example.myapp.Membership.dto.UserInfoResponse;
import com.example.myapp.Membership.service.UserService;
import com.example.myapp.Membership.util.extractInfoFromToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //유저정보조회 엔드포인트
    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId"); //수정 - > userId 토큰에서만 받아오게함(프론트에서는 모르기때문임)
        UserInfoResponse response = userService.getUserInfo(userId);
        return ResponseEntity.ok(response);
    }
}
