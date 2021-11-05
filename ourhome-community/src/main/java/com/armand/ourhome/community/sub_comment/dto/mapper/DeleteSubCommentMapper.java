package com.armand.ourhome.community.sub_comment.dto.mapper;

import com.armand.ourhome.community.sub_comment.dto.response.DeleteSubCommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeleteSubCommentMapper {

    DeleteSubCommentMapper INSTANCE = Mappers.getMapper(DeleteSubCommentMapper.class);

    DeleteSubCommentResponse subCommentToDeleteSubCommentResponse(Long destroySubCommentId, Long destroyCommentId);
}
