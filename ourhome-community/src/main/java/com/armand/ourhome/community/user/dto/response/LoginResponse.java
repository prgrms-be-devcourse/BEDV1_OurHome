package com.armand.ourhome.community.user.dto.response;

import com.armand.ourhome.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    // 만약 실제라면 토큰을 반환해줌
    private Long token;

    public static LoginResponse of(User user){
        return LoginResponse.builder()
                .token(user.getId())
                .build();
    }
}
