package com.armand.ourhome.domain.user;

import com.armand.ourhome.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name="password", nullable = false, length = 50)
    private String password;

    @Column(name="nickname", nullable = false, length = 50, unique = true)
    private String nickname;

    @Column(name="address", length = 100)
    private String address;

    @Column(name="description", length = 100)
    private String description;

    @Column(name="profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;
}
