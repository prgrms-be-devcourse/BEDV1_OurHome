package com.armand.ourhome.community.sub_comment.dto.response;

import com.armand.ourhome.community.comment.dto.response.CreateCommentResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateSubCommentResponse {

    private Long id;
    private String subComment;
    private String createdAt;
    private CreateCommentResponse.Writer writer;

    @Getter
    @Builder
    @JsonInclude(Include.NON_NULL)
    public static class Writer {

        private Long id;
        private String nickname;
        private String profileImageUrl;
    }
}
