package com.armand.ourhome.community.sub_comment.repository;

import com.armand.ourhome.community.comment.entity.Comment;
import com.armand.ourhome.community.sub_comment.entity.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubCommentRepository extends JpaRepository<SubComment, Long> {

    Long countByComment(Comment comment);

    @Query("select count(sc) from SubComment sc where sc.comment in (:commentList)")
    Long countByCommentList(@Param("commentList") List<Comment> commentList);
}
