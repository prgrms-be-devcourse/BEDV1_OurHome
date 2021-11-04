package com.armand.ourhome.community.follow.service;

import com.armand.ourhome.common.error.exception.InvalidValueException;
import com.armand.ourhome.common.error.exception.user.UserNotFoundException;
import com.armand.ourhome.community.follow.entity.Follow;
import com.armand.ourhome.community.follow.repository.FollowRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public void follow(Long followingId, Long myId) {
        User follower = userRepository.findById(myId).get();
        User following = userRepository.findById(followingId).orElseThrow(() -> new UserNotFoundException(followingId));
        if (followRepository.existsByFollowerAndFollowing(follower, following))
            // 이렇게 사용자 id를 알려주는게 맞을까? 로깅할때만 써주면 될지도?
            throw new InvalidValueException(MessageFormat.format("이미 팔로우하고 있는 사용자입니다. (id : {})", followingId));
        followRepository.save(
                Follow.builder()
                        .follower(follower)
                        .following(following)
                        .build()
        );
    }

    @Transactional
    public void unfollow(Long followingId, Long myId) {
        User follower = userRepository.findById(myId).get();
        User following = userRepository.findById(followingId).orElseThrow(() -> new UserNotFoundException(followingId));
        if (!followRepository.existsByFollowerAndFollowing(follower, following))
            throw new InvalidValueException(MessageFormat.format("이미 언팔로우한 사용자입니다. (id : {})", followingId));
        followRepository.deleteByFollowerAndFollowing(follower, following);
    }

}
