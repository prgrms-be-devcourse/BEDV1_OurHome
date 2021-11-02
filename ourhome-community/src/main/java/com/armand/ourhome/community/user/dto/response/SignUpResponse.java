package com.armand.ourhome.community.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming
public class SignUpResponse {
    private Long token;

    private LocalDateTime createdAt;
}
