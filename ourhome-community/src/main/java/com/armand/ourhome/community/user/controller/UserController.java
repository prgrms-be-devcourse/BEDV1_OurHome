package com.armand.ourhome.community.user.controller;


import com.armand.ourhome.community.user.dto.request.LoginRequest;
import com.armand.ourhome.community.user.dto.request.SignUpRequest;
import com.armand.ourhome.community.user.dto.request.UpdateInfoRequest;
import com.armand.ourhome.community.user.dto.response.LoginResponse;
import com.armand.ourhome.community.user.dto.response.SignUpResponse;
import com.armand.ourhome.community.user.dto.response.UpdateInfoResponse;
import com.armand.ourhome.community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    // 회원정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<UpdateInfoResponse> updateInfo(
            @PathVariable(value = "id") Long id,
            @Valid @RequestBody UpdateInfoRequest request
    ) throws IOException {
        UpdateInfoResponse response = userService.updateInfo(id, request);
        return ResponseEntity.ok(response);
    }



}
