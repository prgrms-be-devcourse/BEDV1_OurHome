package com.armand.ourhome.domain.user;

import com.armand.ourhome.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name="nickname", nullable = false, length = 30, unique = true)
    private String nickname;

    @Column(name="address", length = 100)
    private String address;

    @Column(name="description", length = 100)
    private String description;

    @Column(name="profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;

    @Builder
    public User(LocalDateTime createdAt, LocalDateTime updatedAt, Long id, String email, String password, String nickname, String address, String description, String profileImageUrl) {
        super(createdAt, updatedAt);
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
    }

    // ------------------------------------------------------------------------------------------------------------

    public void updateInfo(String nickname, String description, String profileImageUrl){
        this.nickname = nickname;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
    }

    public void updatePassword(String password){
        this.password = password;
    }

}
