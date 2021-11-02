package com.armand.ourhome.community.user.dto.request;

import com.armand.ourhome.community.user.dto.request.validation.UserValidationGroups.*;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
public class LoginRequest {
    @Email(message = "이메일 형식이 맞지 않습니다", groups = EmailGroup.class)
    @NotBlank(message = "이메일을 입력해주세요", groups = NotBlankEmailGroup.class)
    private String email;

    @Size(message="비밀번호는 8~45자까지 입력 가능합니다", min=8, max=45 , groups = PasswordSizeGroup.class)
    @NotBlank(message = "비밀번호를 입력해주세요", groups = NotBlankPasswordGroup.class)
    private String password;
}
