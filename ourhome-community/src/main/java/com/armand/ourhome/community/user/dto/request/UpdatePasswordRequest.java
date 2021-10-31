package com.armand.ourhome.community.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor  // 필드값이 1개인 request에는 기본 생성자를 달아줘야함
@Getter
public class UpdatePasswordRequest {
    @Size(message="비밀번호는 8~45자까지 입력 가능합니다", min=8, max=45)
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
}
