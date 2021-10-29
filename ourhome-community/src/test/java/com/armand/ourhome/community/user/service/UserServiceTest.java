package com.armand.ourhome.community.user.service;

import com.armand.ourhome.community.user.dto.request.SignUpRequest;
import com.armand.ourhome.community.user.dto.response.SignUpResponse;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private User saveUser(){
        User user = User.builder()
                .email("test@mail.com")
                .password("1234")
                .nickname("test")
                .description("오늘의집 클론코딩 좋아하는 사람")
                .build();
        return userRepository.save(user);
    }

    @AfterEach
    void cleanUp(){
        userRepository.deleteAll();
    }

// -----------------------------------------------------------------------------------------

    @Test
    void 회원가입을_할수있다(){
        // Given
        saveUser();
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("new@mail.com")
                .password("1111")
                .nickname("new-user")
                .build();

        // When
        SignUpResponse response = userService.signUp(signUpRequest);

        // Then
        assertThat(userRepository.count(), is(2L));
    }


}