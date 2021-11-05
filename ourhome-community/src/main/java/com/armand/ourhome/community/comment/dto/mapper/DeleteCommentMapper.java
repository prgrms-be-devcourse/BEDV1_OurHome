package com.armand.ourhome.community.comment.dto.mapper;

import com.armand.ourhome.community.comment.dto.response.DeleteCommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeleteCommentMapper {

    DeleteCommentMapper INSTANCE = Mappers.getMapper(DeleteCommentMapper.class);

    DeleteCommentResponse commentToDeleteCommentResponse(Long removeCommentId, Long destroyCommentId);
}
