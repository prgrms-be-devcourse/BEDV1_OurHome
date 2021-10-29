package com.armand.ourhome.community.post.entity;

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