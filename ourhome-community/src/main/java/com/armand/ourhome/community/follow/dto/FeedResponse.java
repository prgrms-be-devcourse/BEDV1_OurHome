package com.armand.ourhome.community.follow.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FeedResponse {
    private String profileImageUrl;
    private String nickname;
    private String mediaUrl;
    private String description;
    private List<String> tagList;
    private Long likeCount;
    private Boolean isLike;
    private Long bookmarkCount;
    private Boolean isBookmark;
    private Long commentCount;
}
