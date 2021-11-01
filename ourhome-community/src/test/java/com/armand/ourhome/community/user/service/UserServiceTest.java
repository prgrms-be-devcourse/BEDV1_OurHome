//package com.armand.ourhome.community.user.service;
//
//import com.armand.ourhome.community.user.dto.request.SignUpRequest;
//import com.armand.ourhome.community.user.dto.response.SignUpResponse;
//import com.armand.ourhome.domain.user.User;
//import com.armand.ourhome.domain.user.UserRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//
//@Transactional
//@SpringBootTest
//class UserServiceTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserService userService;
//
//    private User createUser(){
//        User user = User.builder()
//                .id(1L)
//                .email("test@mail.com")
//                .password("1234")
//                .nickname("test")
//                .description("오늘의집 클론코딩 좋아하는 사람")
//                .profileImageUrl("test url")
//                .build();
//        return userRepository.save(user);
//    }
//
//    @AfterEach
//    void cleanUp(){
//        userRepository.deleteAll();
//    }
//
//// -----------------------------------------------------------------------------------------
//
//    @Test
//    void 회원가입_가능(){
//        // Given
//        SignUpRequest signUpRequest = SignUpRequest.builder()
//                .email("test2@mail.com")
//                .password("1234")
//                .nickname("test2")
//                .build();
//        // When
//        SignUpResponse response = userService.signUp(signUpRequest);
//
//        // Then
//        assertThat(userRepository.count(), is(1L));
//    }
//
//    @Test
//    void 회원가입_이메일중복(){
//        // Given
//        SignUpRequest signUpRequest = SignUpRequest.builder()
//                .email("test@mail.com")
//                .password("1234")
//                .nickname("test2")
//                .build();
//    }
//
//
//
//}