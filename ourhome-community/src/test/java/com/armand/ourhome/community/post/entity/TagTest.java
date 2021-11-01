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
class TagTest {


    @Test
    @DisplayName("tag entity를 생성할 수 있다. ")
    void createTagEntity(){
        //When, Then
        Tag.builder()
                .name("태크")
                .build();

    }

    @Test
    @DisplayName("name은 null 값을 허용하지 않는다.")
    void validCheckNOTNULL_name(){
        //When, Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Tag.builder()
                    .build();
        });
    }

    @Test
    @DisplayName("name은 30자 이상의 값을 허용하지 않는다.")
    void validCheckLength_name(){
        //When, Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Tag.builder()
                    .name("테그테그테그테그테그테그테그테그테그테그테그테그테그테그테그테그테그테그테그테그테그테그테그테그테그")
                    .build();
        });
    }
}