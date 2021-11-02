package com.armand.ourhome.community.user.dto.response;

import com.armand.ourhome.domain.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    // 만약 실제라면 토큰을 반환해줌
    @JsonProperty("user_id")
    private Long id;

    public static LoginResponse of(User user){
        return LoginResponse.builder()
                .id(user.getId())
                .build();
    }
}
