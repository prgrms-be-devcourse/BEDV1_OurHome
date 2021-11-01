package com.armand.ourhome.community.user.service;

import com.armand.ourhome.community.user.dto.request.SignUpRequest;
import com.armand.ourhome.community.user.dto.response.SignUpResponse;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    // @Mock 객체를 해당 객체에 DI함
    @InjectMocks
    private UserService userService;

    User user = User.builder()
            .id(1L)
            .email("test@mail.com")
            .password("1234")
            .nickname("test")
            .createdAt(LocalDateTime.now())
            .build();

//// -----------------------------------------------------------------------------------------

    @Test
    void 회원가입을_할수있다() {
        // Given
        // userRepository.save의 리턴값을 미리 정의
        given(userRepository.save(any())).willReturn(user);
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("test@mail.com")
                .password("1234")
                .nickname("test")
                .build();

        // When
        SignUpResponse result = userService.signUp(signUpRequest);

        // Then
        assertThat(result.getId(), is(user.getId()));
        assertThat(result.getCreatedAt(), is(user.getCreatedAt()));
    }


}