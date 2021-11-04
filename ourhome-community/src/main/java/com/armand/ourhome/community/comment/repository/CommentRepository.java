package com.armand.ourhome.community.comment.repository;

import com.armand.ourhome.community.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
