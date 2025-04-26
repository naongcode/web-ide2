package com.example.myapp.Membership.controller;

import com.example.myapp.Membership.dto.UserInfoResponse;
import com.example.myapp.Membership.service.UserService;
import com.example.myapp.Membership.util.ExtractInfoFromToken;
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

    // 유저 정보 조회 엔드포인트
    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(HttpServletRequest request) {
        // 수정 -> Authorization 헤더에서 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값
        String userId = ExtractInfoFromToken.extractUserIdFromToken(token);

        UserInfoResponse response = userService.getUserInfo(userId);
        return ResponseEntity.ok(response);
    }
}
