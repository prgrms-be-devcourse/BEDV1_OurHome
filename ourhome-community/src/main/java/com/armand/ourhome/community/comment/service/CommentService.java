package com.armand.ourhome.community.comment.service;

import com.armand.ourhome.community.comment.dto.mapper.CreateCommentMapper;
import com.armand.ourhome.community.comment.dto.request.CreateCommentRequest;
import com.armand.ourhome.community.comment.dto.response.CreateCommentResponse;
import com.armand.ourhome.community.comment.entity.Comment;
import com.armand.ourhome.community.comment.repository.CommentRepository;
import com.armand.ourhome.community.exception.PostNotFountException;
import com.armand.ourhome.community.exception.UserNotFountException;
import com.armand.ourhome.community.post.entity.Post;
import com.armand.ourhome.community.post.repository.PostRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public CreateCommentResponse createComment(CreateCommentRequest createCommentRequest) {
        Long userId = createCommentRequest.getUserId();
        Long postId = createCommentRequest.getPostId();

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFountException(userId));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFountException(postId));

        Comment comment = CreateCommentMapper.INSTANCE.createCommentRequestToComment(createCommentRequest, user, post);
        Comment saveComment = commentRepository.save(comment);

        return CreateCommentMapper.INSTANCE.commentToCreateCommentResponse(saveComment);
    }
}
