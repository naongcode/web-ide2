package com.example.myapp.Membership.controller;

import com.example.myapp.Membership.dto.*;
import com.example.myapp.Membership.entity.User2;
import com.example.myapp.Membership.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    //회원가입 엔드포인트
    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        try {
            User2 user2 = userService.register(request);
            //신규
            RegisterResponse response = new RegisterResponse("회원가입 성공");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalStateException e) {
            // 이미 존재하는 경우
            RegisterResponse errorResponse = new RegisterResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            // 예상치 못한 에러 처리
            RegisterResponse errorResponse = new RegisterResponse("서버 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    // 아이디 중복 엔드포인트
    @GetMapping("/check-id/{user_id}")
    public ResponseEntity<?> checkUserIdDuplicate(HttpServletRequest request, @PathVariable("user_id") String pathUserId) {
        try {
            //아이디 중복확인 로직
            boolean isDuplicate = userService.isUserIdDuplicate(pathUserId);
            return ResponseEntity.ok(new CheckUserIdResponse(isDuplicate));

        } catch (Exception e) {
            log.error("아이디 중복 확인 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CheckUserIdResponse(false));
        }
    }



    //로그인 엔드포인트
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try{
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("Message","서버오류: " + e.getMessage()));
        }

    }

    //아이디 찾기 엔드포인트
    @PostMapping("/find-id")
    public ResponseEntity<FindIdResponse> findUserId(@RequestBody FindIdRequest request) {
        FindIdResponse response = userService.findUserIdByEmail(request);
        return ResponseEntity.ok(response);
    }

    //비밀번호 찾기 엔드포인트
    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody FindPasswordRequest request) {
        try {
            userService.resetPasswordAndSendEmail(request);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }


    //로그아웃 엔드포인트
    @PostMapping("/logout")
    public ResponseEntity<?> logout()
    {
        return ResponseEntity.ok(Map.of("message", "로그아웃됐습니다."));

    }


}

