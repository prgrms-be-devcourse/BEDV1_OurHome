package com.armand.ourhome.community.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPageResponse {
    private String nickname;
    private String description;
    private Long followerCount;
    private Long followingCount;
}
