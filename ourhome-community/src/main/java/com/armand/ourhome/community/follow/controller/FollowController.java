package com.armand.ourhome.community.follow.controller;

import com.armand.ourhome.community.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/follows")
@RestController
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{id}")
    public ResponseEntity<Void> follow(
            @PathVariable(value = "id") Long followingId,
            @RequestParam(value = "token") Long token
    ) {
        followService.follow(followingId, token);
        return ResponseEntity.ok().build();
    }

//    @DeleteMapping()
//    public ResponseEntity<Void> unfollow() {
//        return ResponseEntity.ok().build();
//    }

}
