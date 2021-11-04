package com.armand.ourhome.community.sub_comment.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateSubCommentRequest {

    private static final int MAX_LENGTH_OF_SUB_COMMENT = 300;

    @NotBlank(message = "답글 내용을 입력해주세요.")
    @Size(message = "답글 내용은 300자 이내로 작성해주세요.", max = MAX_LENGTH_OF_SUB_COMMENT)
    private String subComment;

    @NotNull(message = "사용자 아이디는 필수 항목입니다.")
    private Long userId;

    @NotNull(message = "댓글 아이디는 필수 항목입니다.")
    private Long commentId;
}
