package com.armand.ourhome.community.comment.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCommentRequest {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    @Size(message = "댓글 내용은 300자 이내로 작성해주세요.", max = 300)
    private String comment;

    @NotNull(message = "사용자 아이디는 필수 항목입니다.")
    private Long userId;

    @NotNull(message = "게시물 아이디는 필수 항목입니다.")
    private Long postId;
}
