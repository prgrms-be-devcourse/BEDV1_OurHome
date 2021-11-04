package com.armand.ourhome.community.sub_comment.controller;

import com.armand.ourhome.community.sub_comment.dto.request.CreateSubCommentRequest;
import com.armand.ourhome.community.sub_comment.dto.response.CreateSubCommentResponse;
import com.armand.ourhome.community.sub_comment.service.SubCommentService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sub-comments")
@RequiredArgsConstructor
public class SubCommentController {

    private final SubCommentService subCommentService;

    @PostMapping
    public ResponseEntity<CreateSubCommentResponse> createSubComment(
        @Valid @RequestBody final CreateSubCommentRequest createSubCommentRequest) {
        final CreateSubCommentResponse createSubCommentResponse = subCommentService.createSubComment(
            createSubCommentRequest);
        return ResponseEntity.ok(createSubCommentResponse);
    }
}
