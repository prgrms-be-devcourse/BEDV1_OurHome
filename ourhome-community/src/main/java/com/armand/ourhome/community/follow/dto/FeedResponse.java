package com.armand.ourhome.community.follow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FeedResponse {
    @JsonProperty("post_id")
    private Long id;
    private String profileImageUrl;
    private String nickname;
    private List<String> mediaUrlList;
    private String description;
    private List<String> tagList;
    private Long likeCount;
    private Boolean isLike;
    private Long bookmarkCount;
    private Boolean isBookmark;
    private Long allCommentCount;
}
