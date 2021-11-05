package com.armand.ourhome.community.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class DeleteCommentResponse {

    private Long removeCommentId;
    private Long destroyCommentId;
}
