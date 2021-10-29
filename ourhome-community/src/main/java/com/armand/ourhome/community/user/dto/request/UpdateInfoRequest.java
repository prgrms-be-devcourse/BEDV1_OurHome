package com.armand.ourhome.community.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

@Getter
@Builder
public class UpdateInfoRequest {
    @Size(message="닉네임은 2~15자까지 입력 가능합니다", min=2, max=15)
    private String nickname;

    private String description;

    private MultipartFile profileImage;
}
