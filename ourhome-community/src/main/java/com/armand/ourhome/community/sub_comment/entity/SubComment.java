package com.armand.ourhome.community.sub_comment.entity;

import com.armand.ourhome.community.comment.entity.Comment;
import com.armand.ourhome.community.comment.util.CommentDateTimeFormatter;
import com.armand.ourhome.domain.base.BaseEntity;
import com.armand.ourhome.domain.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "sub_comment")
public class SubComment extends BaseEntity {

    private static final int MAX_LENGTH_OF_SUB_COMMENT = 300;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sub_comment", length = MAX_LENGTH_OF_SUB_COMMENT, nullable = false)
    private String subComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "id", nullable = false)
    private Comment comment;

    @Builder
    public SubComment(String subComment, User user, Comment comment) {
        this.subComment = subComment;
        this.user = user;
        this.comment = comment;
    }

    public String getFormatterCreateAt() {
        return new CommentDateTimeFormatter().print(this.getCreatedAt());
    }
}
