package com.armand.ourhome.community.user.dto.response;

import com.armand.ourhome.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
public class UserPageResponse {
    private String nickname;
    private String description;
    private Long followerCount;
    private Long followingCount;
    private Long bookmarkCount;
    private Long likeCount;
    // Map<썸네일 url, 여러장체크>
    private List<Map<String, Boolean>> postInfo;

}
