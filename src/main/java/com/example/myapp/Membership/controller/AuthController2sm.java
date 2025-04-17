//package com.example.myapp.Membership.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController2sm {
//
//    @PostMapping("/signup")
//    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
//        // 요청 정보 출력 (테스트용)
//        System.out.println("회원가입 요청: " + request.userId + ", " + request.nickname + ", " + request.email);
//
//        return ResponseEntity.ok("회원가입 요청 받음!");
//    }
//
//    // 요청 DTO
//    public static class SignupRequest {
//        public String userId;    // userId 필드
//        public String password;   // password 필드
//        public String nickname;   // nickname 필드
//        public String email;      // email 필드
//    }
//}
