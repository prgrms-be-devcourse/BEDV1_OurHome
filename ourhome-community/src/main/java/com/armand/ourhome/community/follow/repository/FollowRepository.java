package com.armand.ourhome.community.follow.repository;

import com.armand.ourhome.community.follow.entity.Follow;
import com.armand.ourhome.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    List<Follow> findByFollowing(User following);

    // Cursor 페이징 (반환값 Page X)
    List<Follow> findByFollowerAndIdLessThanOrderByIdDesc(User follower, Long lastId, Pageable pageable);
    List<Follow> findByFollowingAndIdLessThanOrderByIdDesc(User following, Long lastId, Pageable pageable);
    // 최초 요청시
    List<Follow> findByFollowerOrderByIdDesc(User follower, Pageable pageable);
    List<Follow> findByFollowingOrderByIdDesc(User follower, Pageable pageable);
}
