package com.armand.ourhome.community.sub_comment.dto.mapper;

import com.armand.ourhome.community.sub_comment.dto.response.DestroySubCommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DestroySubCommentMapper {

    DestroySubCommentMapper INSTANCE = Mappers.getMapper(DestroySubCommentMapper.class);

    @Mapping(target = "destroySubCommentId", source = "id")
    DestroySubCommentResponse subCommentToDestroySubCommentResponse(Long id);
}
