package com.armand.ourhome.community.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SignUpResponse {
    private Long id;
    private LocalDateTime createdAt;
}
