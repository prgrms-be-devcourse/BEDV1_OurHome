package com.armand.ourhome.community.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
public class SignUpRequest {
    @Email(message = "이메일 형식이 맞지 않습니다")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(message="비밀번호는 최대 45자까지 입력 가능합니다", max=45)
    private String password;

    @Size(message="닉네임은 2~15자까지 입력 가능합니다", min=2, max=15)
    private String nickname;
}