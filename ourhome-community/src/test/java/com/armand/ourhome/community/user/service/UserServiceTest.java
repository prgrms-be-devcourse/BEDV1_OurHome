package com.armand.ourhome.community.user.service;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;
import com.armand.ourhome.common.error.exception.InvalidValueException;
import com.armand.ourhome.common.utils.AwsS3Uploader;
import com.armand.ourhome.community.bookmark.repository.BookmarkRepository;
import com.armand.ourhome.community.follow.repository.FollowRepository;
import com.armand.ourhome.community.like.repository.LikeRepository;
import com.armand.ourhome.community.post.entity.*;
import com.armand.ourhome.community.post.repository.PostRepository;
import com.armand.ourhome.community.user.dto.request.LoginRequest;
import com.armand.ourhome.community.user.dto.request.SignUpRequest;
import com.armand.ourhome.community.user.dto.request.UpdateInfoRequest;
import com.armand.ourhome.community.user.dto.request.UpdatePasswordRequest;
import com.armand.ourhome.community.user.dto.response.LoginResponse;
import com.armand.ourhome.community.user.dto.response.SignUpResponse;
import com.armand.ourhome.community.user.dto.response.UpdateResponse;
import com.armand.ourhome.community.user.dto.response.UserInfoResponse;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AwsS3Uploader awsS3Uploader;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private FollowRepository followRepository;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private BookmarkRepository bookmarkRepository;

    // @Mock ????????? ?????? ????????? DI???
    @InjectMocks
    private UserService userService;

    Long id = 1L;
    String email = "test@mail.com";
    String password = "12341234";
    String nickname = "test";
    String description = "????????? ?????????";
    LocalDateTime createdAt = LocalDateTime.now();

    User user = User.builder()
            .id(id)
            .email(email)
            .password(password)
            .nickname(nickname)
            .description(description)
            .createdAt(createdAt)
            .build();

//// -----------------------------------------------------------------------------------------

    @Test
    void ????????????() {
        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
        // userRepository.save??? ???????????? ?????? ??????
        given(userRepository.save(any())).willReturn(user);

        // When
        SignUpResponse result = userService.signUp(signUpRequest);

        // Then
        assertThat(result.getId(), is(user.getId()));
        assertThat(result.getCreatedAt(), is(user.getCreatedAt()));
    }

    @Test
    void ????????????_?????????_??????() {
        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
        given(userRepository.existsByEmail(email)).willReturn(true);

        // When + Then
        assertThrows(InvalidValueException.class, () -> userService.signUp(signUpRequest));
    }

    @Test
    void ????????????_?????????_??????() {
        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
        given(userRepository.existsByNickname(nickname)).willReturn(true);

        // When + Then
        assertThrows(InvalidValueException.class, () -> userService.signUp(signUpRequest));
    }

    @Test
    void ?????????() {
        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        // When
        LoginResponse result = userService.login(loginRequest);

        // Then
        assertThat(result.getToken(), is(user.getId()));
    }

    @Test
    void ?????????_????????????_??????_?????????() {
        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        // When + Then
        assertThrows(EntityNotFoundException.class, () -> userService.login(loginRequest));
    }

    @Test
    void ?????????_????????????_?????????() {
        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password("dummy")
                .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        // When + Then
        assertThrows(InvalidValueException.class, () -> userService.login(loginRequest));
    }

    @Test
    void ?????????_??????() {
        // Given
        UpdateInfoRequest updateInfoRequest = UpdateInfoRequest.builder()
                .nickname("??????????????????")
                .description("???????????????")
                .profileImageBase64(null)
                .build();
        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(awsS3Uploader.upload(null, "profile")).willReturn(null);

        // When
        UpdateResponse result = userService.updateInfo(id, updateInfoRequest);

        // Then
        assertThat(result, not(nullValue()));
    }

    @Test
    void ?????????_??????_?????????_??????() {
        // Given
        UpdateInfoRequest updateInfoRequest = UpdateInfoRequest.builder()
                .nickname("??????????????????")
                .description("???????????????")
                .profileImageBase64(null)
                .build();
        given(userRepository.existsByNickname("??????????????????")).willReturn(true);

        // When + Then
        assertThrows(InvalidValueException.class, () -> userService.updateInfo(id, updateInfoRequest));
    }

    @Test
    void ????????????_??????() {
        // Given
        UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
                .password("???????????????")
                .build();
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        // When
        UpdateResponse result = userService.updatePassword(id, updatePasswordRequest);

        // Then
        assertThat(result, not(nullValue()));
    }

    @Test
    void ??????????????????_???????????????() {
        // Given
        Pageable pageable = PageRequest.of(0, 8, Sort.by("id").descending());
        Page<Post> page = new PageImpl<>(List.of(), pageable, 0);
        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(postRepository.findAllByUser(user, pageable)).willReturn(page);
        given(followRepository.countByFollower(user)).willReturn(0L);
        given(followRepository.countByFollowing(user)).willReturn(0L);
        given(postRepository.countAllByUser(user)).willReturn(0L);

        given(bookmarkRepository.countByUser(user)).willReturn(0L);
        given(likeRepository.countByUser(user)).willReturn(0L);

        // When
        UserInfoResponse result = userService.userPage(1L, 1L, pageable);

        // Then
        assertThat(result.getNickname(), is(nickname));
        assertThat(result.getDescription(), is(description));
        assertThat(result.getFollowerCount(), is(0L));
        assertThat(result.getFollowingCount(), is(0L));
        assertThat(result.getBookmarkCount(), is(0L));
        assertThat(result.getLikeCount(), is(0L));
        assertThat(result.getPostCount(), is(0L));
        assertThat(result.getThumbnailList(), hasSize(0));
    }

//    @Test
//    void ??????????????????_??????????????????() {
//        // Given
//        Pageable pageable = PageRequest.of(0, 8, Sort.by("id").descending());
//        Page<Post> page = new PageImpl<>(List.of(), pageable, 0);
//        given(userRepository.findById(id)).willReturn(Optional.of(user));
//        given(postRepository.findAllByUser(user, pageable)).willReturn(page);
//        given(followRepository.countByFollower(user)).willReturn(0L);
//        given(followRepository.countByFollowing(user)).willReturn(0L);
//        given(postRepository.countAllByUser(user)).willReturn(0L);
//
//        given(userRepository.findById(2L)).willReturn(Optional.of(???));
//        given(followRepository.existsByFollowerAndFollowing(any(), any())).willReturn(false);
//
//
//        // When -> 2??? ????????? 1??? ????????? ????????? ???????????? ??? ???
//        UserInfoResponse result = userService.userPage(1L, 2L, pageable);
//
//        // Then
//        assertThat(result.getNickname(), is(nickname));
//        assertThat(result.getDescription(), is(description));
//        assertThat(result.getFollowerCount(), is(0L));
//        assertThat(result.getFollowingCount(), is(0L));
//        assertThat(result.getBookmarkCount(), is(nullValue()));
//        assertThat(result.getLikeCount(), is(nullValue()));
//        assertThat(result.getPostCount(), is(0L));
//        assertThat(result.getThumbnailList(), hasSize(0));
//        assertThat(result.getIsFollowing(), is(false));
//    }

}