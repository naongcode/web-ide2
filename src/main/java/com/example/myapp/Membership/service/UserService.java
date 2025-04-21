package com.example.myapp.Membership.service;

import com.example.myapp.Membership.dto.*;
import com.example.myapp.Membership.entity.TeamMember2;
import com.example.myapp.Membership.repository.TeamMemberRepository2;
import com.example.myapp.Membership.util.JwtTokenProvider;
import com.example.myapp.Membership.entity.User2;
import com.example.myapp.Membership.repository.UserRepository2;
import com.example.myapp.Membership.util.PasswordUtil;
import com.example.myapp.Membership.util.TierUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional
public class UserService {

    private final UserRepository2 userRepository;
    private final EmailService emailService;
    private final TeamMemberRepository2 teamMemberRepository2;
    private final SolvedAcService solvedAcService;

    public UserService(UserRepository2 userRepository, EmailService emailService, TeamMemberRepository2 teamMemberRepository2, SolvedAcService solvedAcService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.teamMemberRepository2 = teamMemberRepository2;
        this.solvedAcService = solvedAcService;
    }

    //회원가입 로직
    public User2 register(RegisterRequest request) {
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        //bycrpt 해싱
        String password = PasswordUtil.hashPassword(request.getPassword());

        int tierNumber = solvedAcService.fetchTier(request.getUserId());
        String tier = TierUtil.convertTier(tierNumber);
        Date lastTierUpdatedAt = new Date();

        User2 user2 = User2.builder()
                .userId(request.getUserId())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .Password(password)
                .tier(tier)
                .lastTierUpdatedAt(lastTierUpdatedAt)
                .build();

        return userRepository.save(user2);
    }

    // 아이디 중복 로직
    public boolean isUserIdDuplicate(String userId) {
        try {
            return userRepository.existsByUserId(userId);  // 예시: 유저 존재 여부 체크
        } catch (Exception e) {
            // 로그로 예외를 찍어서 문제 추적
            log.error("Error checking duplicate user ID: ", e);
            throw new RuntimeException("Database error occurred");
        }
    }

    //로그인 로직
    public LoginResponse login(LoginRequest request) {
        User2 user2 = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("아이디가 잘못되었습니다."));

        String hashedPassword = PasswordUtil.hashPassword(request.getPassword());

        // bcrypt 비밀번호 비교
        if (!PasswordUtil.verifyPassword(request.getPassword(), user2.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        }

        // 티어 갱신 로직 추가
        Date now = new Date();
        Date lastUpdated = user2.getLastTierUpdatedAt();

        boolean shouldUpdateTier = (lastUpdated == null) ||
                (now.getTime() - lastUpdated.getTime() > 24 * 60 * 60 * 1000L); // 24시간 지났는지

        if (shouldUpdateTier) {
            try {
                int tierNumber = solvedAcService.fetchTier(user2.getUserId());
                String latestTier = TierUtil.convertTier(tierNumber);
                user2.setTier(latestTier);
                user2.setLastTierUpdatedAt(now);
                userRepository.save(user2);
            } catch (Exception e) {
                log.warn("티어 정보 갱신 실패 (무시하고 로그인 진행): {}", e.getMessage());
            }
        }

        // JWT 생성(아이디와 티어정보를 같이 가지고 있음)
        String token = JwtTokenProvider.createToken(user2.getUserId(), user2.getTier());

        return new LoginResponse(token, "로그인 성공");
    }

    //아이디 찾기 로직
    public FindIdResponse findUserIdByEmail(FindIdRequest request) {
        User2 user2 = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일에 대한 아이디가 없습니다."));
        return new FindIdResponse(user2.getUserId());
    }

    //비밀번호 재설정하여 이메일로 보내는 로직
    public void resetPasswordAndSendEmail(FindPasswordRequest request) {
        User2 user2 = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않습니다."));

        if (!user2.getEmail().equals(request.getEmail())) {
            throw new IllegalArgumentException("이메일이 일치하지 않습니다.");
        }

        //임시 비밀번호 생성하는 로직
        String tempPassword = generateRandomPassword();
        String hashedPassword = PasswordUtil.hashPassword(tempPassword);

        //비밀번호 갱신하는 로직
        user2.setPassword(hashedPassword);
        userRepository.save(user2);

        //이메일 전송(임시 비밀번호도 같이)
        emailService.sendTempPasswordEmail(request.getEmail(), tempPassword);
    }

    //임시 비밀번호 생성 로직
    private String generateRandomPassword() {
        int length = 10;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }


    //유저정보조회 로직
    public UserInfoResponse getUserInfo(String userId) {
        User2 user2 = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다."));

        Optional<TeamMember2> teamMember = teamMemberRepository2.findByUserId_UserId(userId);
        Integer teamId = teamMember.map(tm -> tm.getTeamId().getTeamId()).orElse(null); //수정


        String currentTier;
        try {
            int tierNumber = solvedAcService.fetchTier(userId);
            currentTier = TierUtil.convertTier(tierNumber);
        } catch (Exception e) {
            log.warn("티어 최신 정보 조회 실패, 기존 DB 값 사용: {}", e.getMessage());
            currentTier = user2.getTier();
        }

        return new UserInfoResponse(
                user2.getNickname(),
                user2.getEmail(),
                currentTier,
                teamId
        );
    }
}