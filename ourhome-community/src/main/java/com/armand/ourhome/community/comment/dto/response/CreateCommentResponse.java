package com.armand.ourhome.community.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCommentResponse {

    private Long id;
    private String comment;
    private String createdAt;
    private Writer writer;

    @Getter
    @Builder
    @JsonInclude(Include.NON_NULL)
    public static class Writer {

        private Long id;
        private String nickname;
        private String profileImageUrl;
    }
}
