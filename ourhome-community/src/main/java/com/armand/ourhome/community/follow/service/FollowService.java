package com.armand.ourhome.community.follow.service;

import com.armand.ourhome.common.error.exception.InvalidValueException;
import com.armand.ourhome.common.error.exception.user.UserNotFoundException;
import com.armand.ourhome.community.follow.dto.response.FollowInfoResponse;
import com.armand.ourhome.community.follow.entity.Follow;
import com.armand.ourhome.community.follow.repository.FollowRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public void follow(Long followingId, Long token) {
        User follower = userRepository.findById(token).get();
        User following = userRepository.findById(followingId).orElseThrow(() -> new UserNotFoundException(followingId));
        if (followRepository.existsByFollowerAndFollowing(follower, following))
            throw new InvalidValueException("이미 팔로우하고 있는 사용자입니다.");
        followRepository.save(
                Follow.builder()
                        .follower(follower)
                        .following(following)
                        .build()
        );
    }

    @Transactional
    public void unfollow(Long followingId, Long token) {
        User follower = userRepository.findById(token).get();
        User following = userRepository.findById(followingId).orElseThrow(() -> new UserNotFoundException(followingId));
        if (!followRepository.existsByFollowerAndFollowing(follower, following))
            throw new InvalidValueException("이미 팔로우 해제된 사용자입니다.");
        followRepository.deleteByFollowerAndFollowing(follower, following);
    }

    public List<FollowInfoResponse> followingPage(Long token) {
        User user = userRepository.findById(token).get();
        return followRepository.findByFollower(user).stream()   // 다 들고오기 vs following 컬럼만 들고오기 ???
                .map(follow -> FollowInfoResponse.of(follow.getFollowing(), true))
                .collect(Collectors.toList());
    }

    public List<FollowInfoResponse> followerPage(Long token) {
        User user = userRepository.findById(token).get();
        return followRepository.findByFollowing(user).stream()
                .map(follow ->
                        FollowInfoResponse.of(
                                follow.getFollower(),
                                followRepository.existsByFollowerAndFollowing(user, follow.getFollower())
                        )
                )
                .collect(Collectors.toList());
    }


}
