package com.armand.ourhome.community.user.dto.response;

import com.armand.ourhome.domain.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateResponse {
    private LocalDateTime updatedAt;

    public static UpdateResponse of(User user){
        return UpdateResponse.builder()
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
