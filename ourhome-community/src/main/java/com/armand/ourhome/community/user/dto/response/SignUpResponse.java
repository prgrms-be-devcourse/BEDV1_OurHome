package com.armand.ourhome.community.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SignUpResponse {
    @JsonProperty("user_id")
    private Long id;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
