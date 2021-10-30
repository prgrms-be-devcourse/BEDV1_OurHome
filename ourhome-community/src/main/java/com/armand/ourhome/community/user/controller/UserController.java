package com.armand.ourhome.community.user.controller;


import com.armand.ourhome.common.utils.AwsS3Uploader;
import com.armand.ourhome.community.user.dto.request.SignUpRequest;
import com.armand.ourhome.community.user.dto.response.SignUpResponse;
import com.armand.ourhome.community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;
    private final AwsS3Uploader awsS3Uploader;

    // 회원가입
    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request){
        SignUpResponse response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }

    // 로그인





//    @PostMapping
//    public String test(@RequestParam("img") MultipartFile multipartFile) throws IOException {
//        awsS3Uploader.upload(multipartFile, "user-profiles");
//        return "";
//    }

    // 회원가입
    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request){
        SignUpResponse response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }

}
