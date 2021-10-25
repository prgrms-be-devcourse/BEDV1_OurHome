package com.armand.ourhome.domain.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder
    private User(Long id) {
        this.id = id;
    }

    static public User of(UserDto dummyDto){
        return User.builder()
                .id(dummyDto.getId())
                .build();
    }
}
