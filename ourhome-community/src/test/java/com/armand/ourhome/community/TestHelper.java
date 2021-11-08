package com.armand.ourhome.community;

import com.armand.ourhome.community.follow.entity.Follow;
import com.armand.ourhome.community.like.entity.Like;
import com.armand.ourhome.community.post.entity.*;
import com.armand.ourhome.domain.user.User;

import java.util.List;
import java.util.UUID;

public class TestHelper {

    public static User createUser(){
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return User.builder()
                .email(uuid + "@mail.com")
                .password("12341234")
                .nickname(uuid)
                .description("한줄설명")
                .profileImageUrl("S3 프로필 사진 URL")
                .build();
    }

    public static Post createPost(User user){
        return Post.builder()
                .title("제목 - 작성자id : " + user.getId())
                .squareType(SquareType.SIZE_10_PYEONG)
                .residentialType(ResidentialType.DETACHED_HOUCE)
                .styleType(StyleType.NORDIC_STYPE)
                .user(user)
                .contentList(
                        List.of(
                                Content.builder()
                                        .mediaUrl("S3 Post URL 1")
                                        .description("콘텐츠 설명 1")
                                        .placeType(PlaceType.BATHROOM)
                                        .tags(
                                                List.of(Tag.builder()
                                                        .name("APARTMENT")
                                                        .build())
                                        )
                                        .build(),
                                Content.builder()
                                        .mediaUrl("S3 Post URL 2")
                                        .description("콘텐츠 설명 2")
                                        .placeType(PlaceType.LIVINGROOM)
                                        .tags(
                                                List.of(Tag.builder()
                                                        .name("APARTMENT")
                                                        .build())
                                        )
                                        .build()
                        )
                ).build();
    }

    public static Follow createFollow(User follower, User following){
        return Follow.builder()
                .follower(follower)
                .following(following)
                .build();
    }

}
