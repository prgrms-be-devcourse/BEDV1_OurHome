package com.armand.ourhome.community.comment.dto.mapper;

import com.armand.ourhome.community.comment.dto.response.RemoveCommentResponse;
import com.armand.ourhome.community.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RemoveCommentMapper {

    RemoveCommentMapper INSTANCE = Mappers.getMapper(RemoveCommentMapper.class);

    @Mapping(target = "removeCommentId", source = "id")
    RemoveCommentResponse commentToRemoveCommentResponse(Comment comment);
}
