package com.armand.ourhome.community.comment.dto.mapper;

import com.armand.ourhome.community.comment.dto.request.CreateCommentRequest;
import com.armand.ourhome.community.comment.dto.response.CreateCommentResponse;
import com.armand.ourhome.community.comment.entity.Comment;
import com.armand.ourhome.community.post.entity.Post;
import com.armand.ourhome.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreateCommentMapper {

    CreateCommentMapper INSTANCE = Mappers.getMapper(CreateCommentMapper.class);

    @Mapping(target = "removed", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    Comment createCommentRequestToComment(CreateCommentRequest createCommentRequest, User user, Post post);

    @Mapping(target = "writer", source = "comment.user")
    @Mapping(target = "createdAt", expression = "java(comment.getFormatterCreateAt())")
    CreateCommentResponse commentToCreateCommentResponse(Comment comment);
}
