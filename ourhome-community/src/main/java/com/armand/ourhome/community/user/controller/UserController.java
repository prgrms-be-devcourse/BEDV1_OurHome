package com.armand.ourhome.community.user.controller;


import com.armand.ourhome.common.utils.AwsS3Uploader;
import com.armand.ourhome.community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;
    private final AwsS3Uploader awsS3Uploader;

    @PostMapping
    public String test(@RequestParam("img") MultipartFile multipartFile) throws IOException {
        awsS3Uploader.upload(multipartFile, "user-profiles");
        return "";
    }

}
