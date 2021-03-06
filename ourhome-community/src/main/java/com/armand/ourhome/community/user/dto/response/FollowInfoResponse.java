package com.armand.ourhome.community.user.dto.response;

import com.armand.ourhome.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowInfoResponse {
    private Long userId;
    private String profileImageUrl;
    private String nickname;
    private String description;
    private Boolean isFollowing;    // 현 사용자가 팔로우하고 있는지?

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
