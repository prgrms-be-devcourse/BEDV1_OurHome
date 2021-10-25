package com.armand.ourhome.domain.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private Long id;

    static public UserDto of(User user){
        return UserDto.builder()
                .id(user.getId())
                .build();
    }
}
