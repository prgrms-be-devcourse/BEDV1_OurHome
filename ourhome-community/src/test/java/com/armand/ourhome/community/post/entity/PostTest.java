package com.armand.ourhome.community.post.entity;

import com.armand.ourhome.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by yunyun on 2021/10/29.
 */

@Slf4j
class PostTest {

    @Test
    @DisplayName("title은 null 값을 허용하지 않는다.")
    void validCheckNOTNULL_title(){
        //When, Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
           Post.builder()
                   .squareType(SquareType.SIZE_10_PYEONG)
                   .residentialType(ResidentialType.APARTMENT)
                   .styleType(StyleType.ASIAN_STYPE)
                   .user(User.builder()
                           .email("test@email.com")
                           .password("1223")
                           .nickname("화이팅!!")
                           .description("모두의 집 개발자")
                           .profileImageUrl("/user/idslielfjidjf-jielj19.jpg")
                           .build())
                   .contentList(List.of(
                           Content.builder()
                                   .description("content 설명란")
                                   .placeType(PlaceType.LIVINGROOM)
                                   .tags(List.of(
                                           Tag.builder()
                                                   .name("좋은 거실")
                                                   .build(),
                                           Tag.builder()
                                                   .name("깨끗 거실")
                                                   .build()))
                                   .build()))
                   .build();
        });
    }

    @Test
    @DisplayName("title은 30자 이상을 허용하지 않는다.")
    void validCheckLength_title(){
        //When, Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Post.builder()
                    .title("거실 사진입니다..거실 사진입니다..거실 사진입니다..거실 사진입니다..거실 사진입니다..거실 사진입니다.." +
                            "거실 사진입니다..거실 사진입니다..거실 사진입니다..거실 사진입니다..")
                    .squareType(SquareType.SIZE_10_PYEONG)
                    .residentialType(ResidentialType.APARTMENT)
                    .styleType(StyleType.ASIAN_STYPE)
                    .user(User.builder()
                            .email("test@email.com")
                            .password("1223")
                            .nickname("화이팅!!")
                            .description("모두의 집 개발자")
                            .profileImageUrl("/user/idslielfjidjf-jielj19.jpg")
                            .build())
                    .contentList(List.of(
                            Content.builder()
                                    .description("content 설명란")
                                    .placeType(PlaceType.LIVINGROOM)
                                    .tags(List.of(
                                            Tag.builder()
                                                    .name("좋은 거실")
                                                    .build(),
                                            Tag.builder()
                                                    .name("깨끗 거실")
                                                    .build()))
                                    .build()))
                    .build();
        });
    }

    @Test
    @DisplayName("사용자 정보는 null 값을 허용하지 않는다.")
    void validCheckNOTNULL_user(){
        //When, Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Post.builder()
                    .squareType(SquareType.SIZE_10_PYEONG)
                    .residentialType(ResidentialType.APARTMENT)
                    .styleType(StyleType.ASIAN_STYPE)
                    .title("모드의 집의 거실을 소개합니다!")
                    .contentList(List.of(
                            Content.builder()
                                    .description("content 설명란")
                                    .placeType(PlaceType.LIVINGROOM)
                                    .tags(List.of(
                                            Tag.builder()
                                                    .name("좋은 거실")
                                                    .build(),
                                            Tag.builder()
                                                    .name("깔끔")
                                                    .build()))
                                    .build()))
                    .build();
        });
    }


}