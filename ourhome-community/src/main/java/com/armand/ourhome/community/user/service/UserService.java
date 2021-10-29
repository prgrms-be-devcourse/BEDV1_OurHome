package com.armand.ourhome.community.user.service;


import com.armand.ourhome.common.error.exception.BusinessException;
import com.armand.ourhome.common.error.exception.EntityNotFoundException;
import com.armand.ourhome.common.error.exception.ErrorCode;
import com.armand.ourhome.common.utils.AwsS3Uploader;
import com.armand.ourhome.community.user.dto.mapper.SignUpMapper;
import com.armand.ourhome.community.user.dto.request.LoginRequest;
import com.armand.ourhome.community.user.dto.request.SignUpRequest;
import com.armand.ourhome.community.user.dto.request.UpdateInfoRequest;
import com.armand.ourhome.community.user.dto.response.LoginResponse;
import com.armand.ourhome.community.user.dto.response.SignUpResponse;
import com.armand.ourhome.community.user.dto.response.UpdateInfoResponse;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AwsS3Uploader awsS3Uploader;
    private final SignUpMapper signUpMapper = Mappers.getMapper(SignUpMapper.class);

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest){
        Optional<User> byEmail = userRepository.findByEmail(signUpRequest.getEmail());
        if(byEmail.isPresent())
            // FIXME : duplicate exception 추가하고 변경해야함
            throw new EntityNotFoundException("이미 중복되는 이메일이 있습니다");
        // request -> entity -> response
        User user = signUpMapper.requestToEntity(signUpRequest);
        User save = userRepository.save(user);
        return signUpMapper.entityToResponse(save);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Optional<User> user = userRepository.findByEmail(email);
        // 가입된 email 검증
        if(user.isEmpty())
            throw new EntityNotFoundException("가입되지 않은 이메일입니다.");
        // 비번 검증
        String foundPassword = user.get().getPassword();
        if(!password.equals(foundPassword))
            // FIXME : illegalArg exception 추가하고 변경해야함
            throw new EntityNotFoundException("비밀번호가 틀립니다");
        return LoginResponse.of(user.get());
    }

    @Transactional
    public UpdateInfoResponse updateInfo(Long id, UpdateInfoRequest updateInfoRequest) throws IOException {
        String nickname = updateInfoRequest.getNickname();
        String description = updateInfoRequest.getDescription();
        MultipartFile profileImage = updateInfoRequest.getProfileImage();
        String profileImageUrl = awsS3Uploader.upload(profileImage, "user-profiles");

        User user = userRepository.findById(id).get();
        user.updateUserInfo(nickname, description, profileImageUrl);
        // update된 시간을 받아오기 위해 flush -> 다시 select
        userRepository.flush();
        User updatedUser = userRepository.findById(id).get();
        return UpdateInfoResponse.of(updatedUser);
    }



}
