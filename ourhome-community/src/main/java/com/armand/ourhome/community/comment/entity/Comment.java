package com.armand.ourhome.community.comment.entity;

import com.armand.ourhome.community.comment.util.CommentDateTimeFormatter;
import com.armand.ourhome.community.post.entity.Post;
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
@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    private static final int MAX_LENGTH_OF_COMMENT = 300;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "comment", length = MAX_LENGTH_OF_COMMENT, nullable = false)
    private String comment;

    @Column(name = "removed", columnDefinition = "boolean default false", nullable = false)
    private boolean removed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    private Post post;

    @Builder
    public Comment(String comment, boolean removed, User user, Post post) {
        this.comment = comment;
        this.removed = removed;
        this.user = user;
        this.post = post;
    }

    public void removeComment() {
        final String REMOVE_COMMENT = "삭제된 댓글입니다.";

        this.removed = true;
        this.comment = REMOVE_COMMENT;
    }

    public String getFormatterCreateAt() {
        return new CommentDateTimeFormatter().print(this.getCreatedAt());
    }
}
