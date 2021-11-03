package com.armand.ourhome.community.follow.repository;

import com.armand.ourhome.community.follow.entity.Follow;
import com.armand.ourhome.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 해당 유저의 팔로워 숫자 (누군가 나를 팔로우 하고 있는 수를 셈)
    Long countByFollower(User user);
    // 해당 유저의 팔로잉 숫자
    Long countByFollowing(User user);

    Boolean existsByFollowerAndFollowing(User follower, User following);
}
