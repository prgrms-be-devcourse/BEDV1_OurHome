package com.armand.ourhome.community.user.dto.request;

import com.armand.ourhome.community.user.dto.validation.UserValidationGroups.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
public class UpdateInfoRequest {
    @Size(message="닉네임은 2~15자까지 입력 가능합니다", min=2, max=15, groups = NicknameSizeGroup.class)
    @NotBlank(message = "닉네임을 입력해주세요", groups = NotBlankNicknameGroup.class)
    private String nickname;

    private String description;

    @JsonProperty("profile_image_base64")
    private String profileImageBase64;
}
