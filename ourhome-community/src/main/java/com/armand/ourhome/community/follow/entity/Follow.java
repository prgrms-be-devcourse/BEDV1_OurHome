package com.armand.ourhome.community.follow.entity;

import com.armand.ourhome.domain.base.BaseEntity;
import com.armand.ourhome.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "follow", uniqueConstraints = { @UniqueConstraint(columnNames = {"follower", "following"} ) } )
public class Follow extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "follower", nullable = false, referencedColumnName = "id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "following", nullable = false, referencedColumnName = "id")
    private User following;

    @Builder
    public Follow(LocalDateTime createdAt, LocalDateTime updatedAt, Long id, User follower, User following) {
        super(createdAt, updatedAt);
        this.id = id;
        this.follower = follower;
        this.following = following;
    }
}
