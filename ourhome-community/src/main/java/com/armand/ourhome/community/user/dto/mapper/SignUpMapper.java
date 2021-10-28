package com.armand.ourhome.community.user.dto.mapper;

import com.armand.ourhome.community.user.dto.request.SignUpRequest;
import com.armand.ourhome.community.user.dto.response.SignUpResponse;
import com.armand.ourhome.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SignUpMapper {

    @Mapping(target = "address" , ignore = true)
    @Mapping(target = "description" , ignore = true)
    @Mapping(target = "profileImageUrl" , ignore = true)
    User requestToEntity(SignUpRequest request);

    SignUpResponse entityToResponse(User user);
}
