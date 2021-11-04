package com.armand.ourhome.community.sub_comment.dto.mapper;

import com.armand.ourhome.community.comment.entity.Comment;
import com.armand.ourhome.community.sub_comment.dto.request.CreateSubCommentRequest;
import com.armand.ourhome.community.sub_comment.dto.response.CreateSubCommentResponse;
import com.armand.ourhome.community.sub_comment.entity.SubComment;
import com.armand.ourhome.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreateSubCommentMapper {

    CreateSubCommentMapper INSTANCE = Mappers.getMapper(CreateSubCommentMapper.class);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "comment", source = "comment")
    SubComment createSubCommentRequestToSubComment(
        CreateSubCommentRequest createSubCommentRequest, User user, Comment comment);

    @Mapping(target = "writer", source = "subComment.user")
    @Mapping(target = "createdAt", expression = "java(subComment.getFormatterCreateAt())")
    CreateSubCommentResponse subCommentToCreateSubCommentResponse(SubComment subComment);
}
