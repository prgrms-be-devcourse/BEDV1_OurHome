package com.armand.ourhome.community.follow.controller;

import com.armand.ourhome.common.api.CursorPageRequest;
import com.armand.ourhome.common.api.CursorPageResponse;
import com.armand.ourhome.community.follow.dto.FeedResponse;
import com.armand.ourhome.community.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/follow")
@RestController
public class FollowController {

    private final FollowService followService;

    // 팔로우하기
    @PostMapping("/{id}")
    public ResponseEntity<Void> follow(
            @PathVariable(value = "id") Long followingId,
            @RequestParam(value = "token") Long token
    ) {
        followService.follow(followingId, token);
        return ResponseEntity.ok().build();
    }

    // 언팔로우하기
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unfollow(
            @PathVariable(value = "id") Long followingId,
            @RequestParam(value = "token") Long token
    ) {
        followService.unfollow(followingId, token);
        return ResponseEntity.ok().build();
    }

    // 팔로우 피드
    @GetMapping("/feed")
    public ResponseEntity<CursorPageResponse<List<FeedResponse>>> feedPage(
            @RequestParam(value = "token") Long token,
            CursorPageRequest pageRequest
    ) {
        CursorPageResponse<List<FeedResponse>> response = followService.feedPage(token, pageRequest);
        return ResponseEntity.ok(response);
    }

}
