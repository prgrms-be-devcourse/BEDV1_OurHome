package com.armand.ourhome.community.user.dto.response;

import com.armand.ourhome.domain.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateInfoResponse {
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    static public UpdateInfoResponse of(User user){
        return UpdateInfoResponse.builder()
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
