package com.armand.ourhome.domain.user;

import com.armand.ourhome.domain.base.BaseEntity;
import lombok.*;

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

    @Column(name="nickname", nullable = false, length = 30, unique = true)
    private String nickname;

    @Column(name="address", length = 100)
    private String address;

    @Column(name="description", length = 100)
    private String description;

    @Column(name="profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;

    @Builder
    public User(String email, String password, String nickname, String address, String description, String profileImageUrl) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
    }

// ------------------------------------------------------------------------------------------------------------

    public void updateInfo(String nickname, String description){
        this.nickname = nickname;
        this.description = description;
    }

    public void updateProfile(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }


}
