package com.armand.ourhome.community.user.dto.response;

import com.armand.ourhome.domain.user.User;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@JsonNaming
public class UserPageResponse {
    private String nickname;
    private String description;
    private Long followerCount;
    private Long followingCount;
    private Long bookmarkCount;
    private Long likeCount;
    private Long postCount;
    private List<Thumbnail> thumbnailList;
}
