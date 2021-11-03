package com.armand.ourhome.community.follow.dto.response;

import com.armand.ourhome.domain.user.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FollowInfoResponse {
    private Long userId;
    private String profileImageUrl;
    private String nickname;
    private String description;
    private Boolean isFollowing;

    public static FollowInfoResponse of(User user, Boolean isFollowing){
        return FollowInfoResponse.builder()
                .userId(user.getId())
                .profileImageUrl(user.getProfileImageUrl())
                .nickname(user.getNickname())
                .description(user.getDescription())
                .isFollowing(isFollowing)
                .build();
    }
}
