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
class ContentTest {

    /**
    @Test
    @DisplayName("content entity을 생성할 수 있다.")
    void createContentEntity(){
        //When, Then
        Content.builder()
                .description("content 설명란")
                .mediaUrl("/post/dii920j393jlf.png")
                .placeType(PlaceType.LIVINGROOM)
                .post(Post.builder()
                        .title("우리 집")
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
                        .build())
                .build();

    }

    @Test
    @DisplayName("mediaUrl은 null 값을 허용하지 않는다.")
    void validCheckNOTNULL_mediaUrl(){
        //When, Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Content.builder()
                    .post(Post.builder()
                            .title("우리 집")
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
                            .build())
                    .description("content 설명란")
                    .placeType(PlaceType.LIVINGROOM)
                    .build();
        });
    }

    @Test
    @DisplayName("placeType은 null 값을 허용하지 않는다.")
    void validCheckNOTNULL_placeType(){
        //When, Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Content.builder()
                    .post(Post.builder()
                            .title("우리 집")
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
                            .build())
                    .description("content 설명란")
                    .mediaUrl("/post/dii920j393jlf.png")
                    .build();
        });
    }
    **/


    @Test
    @DisplayName("content entity을 생성할 수 있다.")
    void createContentEntity(){
        //When, Then
        Content.builder()
                .description("content 설명란")
                .mediaUrl("/post/dii920j393jlf.png")
                .placeType(PlaceType.LIVINGROOM)
                .build();

    }
    @Test
    @DisplayName("mediaUrl은 null 값을 허용하지 않는다.")
    void validCheckNOTNULL_mediaUrl(){
        //When, Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Content.builder()
                    .description("content 설명란")
                    .placeType(PlaceType.LIVINGROOM)
                    .tags(List.of(
                            Tag.builder()
                                    .name("좋은 거실")
                                    .build(),
                            Tag.builder()
                                    .name("깔끔 거실")
                                    .build()
                    ))
                    .build();
        });
    }

    @Test
    @DisplayName("placeType은 null 값을 허용하지 않는다.")
    void validCheckNOTNULL_placeType(){
        //When, Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Content.builder()
                    .description("content 설명란")
                    .mediaUrl("/post/dii920j393jlf.png")
                    .tags(List.of(
                            Tag.builder()
                                    .name("좋은 거실")
                                    .build(),
                            Tag.builder()
                                    .name("깔끔 거실")
                                    .build()
                    ))
                    .build();
        });
    }

}