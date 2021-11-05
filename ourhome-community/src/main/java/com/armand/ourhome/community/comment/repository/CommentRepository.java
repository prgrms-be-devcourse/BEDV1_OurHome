package com.armand.ourhome.community.comment.repository;

import com.armand.ourhome.community.comment.entity.Comment;
import com.armand.ourhome.community.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}
