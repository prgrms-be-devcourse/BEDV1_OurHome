package com.armand.ourhome.community.user.service;


import com.armand.ourhome.common.error.exception.BusinessException;
import com.armand.ourhome.common.error.exception.ErrorCode;
import com.armand.ourhome.community.user.dto.mapper.SignUpMapper;
import com.armand.ourhome.community.user.dto.request.SignUpRequest;
import com.armand.ourhome.community.user.dto.response.SignUpResponse;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SignUpMapper signUpMapper = Mappers.getMapper(SignUpMapper.class);

    @Transactional
    public SignUpResponse signUp(SignUpRequest request){
        Optional<User> byEmail = userRepository.findByEmail(request.getEmail());
        if(byEmail.isPresent())
            throw new BusinessException("이미 중복되는 이메일이 있습니다", ErrorCode.INVALID_INPUT_VALUE);
        // request -> entity -> response
        User user = signUpMapper.requestToEntity(request);
        User save = userRepository.save(user);
        return signUpMapper.entityToResponse(save);
    }



}
