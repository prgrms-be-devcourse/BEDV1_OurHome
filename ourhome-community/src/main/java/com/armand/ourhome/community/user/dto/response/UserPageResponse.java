package com.armand.ourhome.community.user.dto.response;

import com.armand.ourhome.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserPageResponse {
    private String nickname;
    private String description;
    private Long followerCount;
    private Long followingCount;
}
