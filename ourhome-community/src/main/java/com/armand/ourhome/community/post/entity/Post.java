package com.armand.ourhome.community.post.entity;

import com.armand.ourhome.domain.base.BaseEntity;
import com.armand.ourhome.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title", nullable = false, length = 50)
    private String title;

    @Column(name="square_type")
    @Enumerated(EnumType.STRING)
    private SquareType squareType;

    @Column(name="residential_type")
    @Enumerated(EnumType.STRING)
    private ResidentialType residentialType;

    @Column(name="style_type")
    @Enumerated(EnumType.STRING)
    private StyleType styleType;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
}
