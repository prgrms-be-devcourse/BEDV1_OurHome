package com.armand.ourhome.community.like.repository;

import com.armand.ourhome.community.like.entity.Like;
import com.armand.ourhome.community.post.entity.Post;
import com.armand.ourhome.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Long countByUser(User user);
    Long countByPost(Post post);
    Boolean existsByPostAndUser(Post post, User user);
}
