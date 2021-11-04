package com.armand.ourhome.community.comment.controller;

import com.armand.ourhome.community.comment.dto.request.CreateCommentRequest;
import com.armand.ourhome.community.comment.dto.response.CreateCommentResponse;
import com.armand.ourhome.community.comment.service.CommentService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CreateCommentResponse> createComment(
        @Valid @RequestBody final CreateCommentRequest createCommentRequest) {
        final CreateCommentResponse createCommentResponse = commentService.createComment(createCommentRequest);
        return ResponseEntity.ok(createCommentResponse);
    }
}
