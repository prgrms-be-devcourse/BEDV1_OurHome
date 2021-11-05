package com.armand.ourhome.community.sub_comment.repository;

import com.armand.ourhome.community.comment.entity.Comment;
import com.armand.ourhome.community.sub_comment.entity.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCommentRepository extends JpaRepository<SubComment, Long> {

    Long countByComment(Comment comment);
}
