package com.armand.ourhome.community.user.dto.response;

import com.armand.ourhome.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateResponse {
    private LocalDateTime updatedAt;

    public static UpdateResponse of(User user){
        return UpdateResponse.builder()
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
