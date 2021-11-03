package com.armand.ourhome.community.follow.repository;

import com.armand.ourhome.community.follow.entity.Follow;
import com.armand.ourhome.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 해당 유저의 팔로워 숫자 (누군가 나를 팔로우 하고 있는 수를 셈)
    Long countByFollower(User user);
    // 해당 유저의 팔로잉 숫자
    Long countByFollowing(User user);
    // 팔로우 관계가 존재하는지 여부
    Boolean existsByFollowerAndFollowing(User follower, User following);
    // 팔로우 관계 삭제
    void deleteByFollowerAndFollowing(User follower, User following);
    // 해당 유저의 팔로우 관계 리스트
    List<Follow> findByFollower(User follower);
}
