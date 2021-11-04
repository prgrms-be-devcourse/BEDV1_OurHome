package com.armand.ourhome.community.user.dto.response;

import com.armand.ourhome.domain.user.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FollowPageResponse {
    private Long userId;
    private String profileImageUrl;
    private String nickname;
    private String description;
    private Boolean isFollowing;    // 현 사용자가 팔로우하고 있는지?

    public static FollowPageResponse of(User user, Boolean isFollowing){
        return FollowPageResponse.builder()
                .userId(user.getId())
                .profileImageUrl(user.getProfileImageUrl())
                .nickname(user.getNickname())
                .description(user.getDescription())
                .isFollowing(isFollowing)
                .build();
    }
}
