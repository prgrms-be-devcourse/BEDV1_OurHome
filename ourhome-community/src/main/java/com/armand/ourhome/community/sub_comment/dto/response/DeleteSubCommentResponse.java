package com.armand.ourhome.community.sub_comment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class DeleteSubCommentResponse {

    private Long destroySubCommentId;
    private Long destroyCommentId;
}
