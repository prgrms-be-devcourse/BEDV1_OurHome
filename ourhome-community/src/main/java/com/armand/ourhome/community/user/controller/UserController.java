package com.armand.ourhome.community.user.controller;


import com.armand.ourhome.community.user.dto.request.LoginRequest;
import com.armand.ourhome.community.user.dto.request.SignUpRequest;
import com.armand.ourhome.community.user.dto.request.UpdateInfoRequest;
import com.armand.ourhome.community.user.dto.request.UpdatePasswordRequest;
import com.armand.ourhome.community.user.dto.validation.UserValidationSequence;
import com.armand.ourhome.community.user.dto.response.LoginResponse;
import com.armand.ourhome.community.user.dto.response.SignUpResponse;
import com.armand.ourhome.community.user.dto.response.UpdateResponse;
import com.armand.ourhome.community.user.dto.response.UserPageResponse;
import com.armand.ourhome.community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(
            @Validated(UserValidationSequence.class) @RequestBody SignUpRequest request
    ) {
        SignUpResponse response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Validated(UserValidationSequence.class) @RequestBody LoginRequest request
    ) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    // 회원정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<UpdateResponse> updateInfo(
            @PathVariable(value = "id") Long id,
            @Validated(UserValidationSequence.class) @RequestBody UpdateInfoRequest request
    ) {
        UpdateResponse response = userService.updateInfo(id, request);
        return ResponseEntity.ok(response);
    }

    // 비밀번호 변경
    @PatchMapping("/{id}/password")
    public ResponseEntity<UpdateResponse> updatePassword(
            @PathVariable(value = "id") Long id,
            @Validated(UserValidationSequence.class) @RequestBody UpdatePasswordRequest request
    ) {
        UpdateResponse response = userService.updatePassword(id, request);
        return ResponseEntity.ok(response);
    }

    // 유저페이지
    @GetMapping("/{id}")
    public ResponseEntity<UserPageResponse> userPage(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "token") Long token,   // 나의 id 값 (대충 토큰이라 부름)
            Pageable pageable
    ) {
        UserPageResponse response = userService.userPage(id, token, pageable);
        return ResponseEntity.ok(response);
    }

}
