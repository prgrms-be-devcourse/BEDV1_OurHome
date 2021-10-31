package com.armand.ourhome.community.user.service;


import com.armand.ourhome.common.error.exception.EntityNotFoundException;
import com.armand.ourhome.common.utils.AwsS3Uploader;
import com.armand.ourhome.community.follow.repository.FollowRepository;
import com.armand.ourhome.community.user.dto.mapper.SignUpMapper;
import com.armand.ourhome.community.user.dto.request.LoginRequest;
import com.armand.ourhome.community.user.dto.request.SignUpRequest;
import com.armand.ourhome.community.user.dto.request.UpdateInfoRequest;
import com.armand.ourhome.community.user.dto.request.UpdatePasswordRequest;
import com.armand.ourhome.community.user.dto.response.LoginResponse;
import com.armand.ourhome.community.user.dto.response.SignUpResponse;
import com.armand.ourhome.community.user.dto.response.UpdateResponse;
import com.armand.ourhome.community.user.dto.response.UserPageResponse;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final AwsS3Uploader awsS3Uploader;
    private final SignUpMapper signUpMapper = Mappers.getMapper(SignUpMapper.class);

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        isDuplicateEmail(email);
        String nickname = signUpRequest.getNickname();
        isDuplicateNickname(nickname);
        // request -> entity -> response
        User user = signUpMapper.requestToEntity(signUpRequest);
        User save = userRepository.save(user);
        return signUpMapper.entityToResponse(save);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Optional<User> user = userRepository.findByEmail(email);
        // 가입된 email 검증
        if (user.isEmpty())
            throw new EntityNotFoundException("가입되지 않은 이메일입니다.");
        // 비번 검증
        String foundPassword = user.get().getPassword();
        if (!password.equals(foundPassword))
            // FIXME : illegalArg exception 추가하고 변경해야함
            throw new EntityNotFoundException("비밀번호가 틀립니다");
        return LoginResponse.of(user.get());
    }

    @Transactional
    public UpdateResponse updateInfo(Long id, UpdateInfoRequest updateInfoRequest) {
        String nickname = updateInfoRequest.getNickname();
        isDuplicateNickname(nickname);
        String description = updateInfoRequest.getDescription();
        String profileImageBase64 = updateInfoRequest.getProfileImageBase64();
        String profileImageUrl = awsS3Uploader.upload(profileImageBase64, "user-profiles");

        User user = userRepository.findById(id).get();
        user.updateInfo(nickname, description, profileImageUrl);

        // update된 시간을 받아오기 위해 flush -> 다시 select
        userRepository.flush();
        User updatedUser = userRepository.findById(id).get();
        return UpdateResponse.of(updatedUser);
    }

    @Transactional
    public UpdateResponse updatePassword(Long id, UpdatePasswordRequest updatePasswordRequest){
        String password = updatePasswordRequest.getPassword();
        User user = userRepository.findById(id).get();
        user.updatePassword(password);
        userRepository.flush();
        User updatedUser = userRepository.findById(id).get();
        return UpdateResponse.of(updatedUser);
    }

    public UserPageResponse userPage(Long id, Long token) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty())
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다");
        User user = byId.get();
        Long followerCount = followRepository.countByFollower(user);
        Long followingCount = followRepository.countByFollowing(user);

        UserPageResponse.UserPageResponseBuilder responseBuilder = UserPageResponse.builder();
        responseBuilder.nickname(user.getNickname())
                .description(user.getDescription())
                .followerCount(followerCount)
                .followingCount(followingCount);
        // mypage일 경우
        if (Objects.equals(id, token)) {
//            responseBuilder.like()
        }
        return responseBuilder.build();
    }

    // ------------------------------------------------------------------------

    private void isDuplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent())
            throw new EntityNotFoundException("이미 중복되는 이메일이 있습니다");
    }

    private void isDuplicateNickname(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent())
            throw new EntityNotFoundException("이미 중복되는 닉네임이 있습니다");
    }

}
