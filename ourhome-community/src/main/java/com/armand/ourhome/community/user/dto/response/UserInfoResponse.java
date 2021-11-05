package com.armand.ourhome.community.user.dto.response;

import com.armand.ourhome.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
public class UserInfoResponse {
    private String nickname;
    private String description;
    private String profileImageUrl;
    private Long followerCount;
    private Long followingCount;
    private Boolean isFollowing;
    private Long bookmarkCount;
    private Long likeCount;
    private Long postCount;
    private List<Thumbnail> thumbnailList;
}
