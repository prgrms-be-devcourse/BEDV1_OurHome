package com.armand.ourhome.community.sub_comment.service;

import com.armand.ourhome.community.comment.entity.Comment;
import com.armand.ourhome.community.comment.repository.CommentRepository;
import com.armand.ourhome.community.exception.CommentNotFountException;
import com.armand.ourhome.community.exception.SubCommentNotFountException;
import com.armand.ourhome.community.exception.UserNotFountException;
import com.armand.ourhome.community.sub_comment.dto.mapper.CreateSubCommentMapper;
import com.armand.ourhome.community.sub_comment.dto.mapper.DeleteSubCommentMapper;
import com.armand.ourhome.community.sub_comment.dto.request.CreateSubCommentRequest;
import com.armand.ourhome.community.sub_comment.dto.response.CreateSubCommentResponse;
import com.armand.ourhome.community.sub_comment.dto.response.DeleteSubCommentResponse;
import com.armand.ourhome.community.sub_comment.entity.SubComment;
import com.armand.ourhome.community.sub_comment.repository.SubCommentRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubCommentService {

    private final SubCommentRepository subCommentRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CreateSubCommentResponse createSubComment(CreateSubCommentRequest createSubCommentRequest) {
        Long userId = createSubCommentRequest.getUserId();
        Long commentId = createSubCommentRequest.getCommentId();

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFountException(userId));
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFountException(commentId));

        SubComment subComment = CreateSubCommentMapper.INSTANCE.createSubCommentRequestToSubComment(
            createSubCommentRequest, user, comment);
        SubComment saveSubComment = subCommentRepository.save(subComment);

        return CreateSubCommentMapper.INSTANCE.subCommentToCreateSubCommentResponse(saveSubComment);
    }

    @Transactional
    public DeleteSubCommentResponse deleteSubComment(Long subCommentId) {
        SubComment subComment = subCommentRepository.findById(subCommentId)
            .orElseThrow(() -> new SubCommentNotFountException(subCommentId));
        subCommentRepository.delete(subComment);

        Comment comment = subComment.getComment();
        if (comment != null && comment.isRemoved()) {
            Long countByComment = subCommentRepository.countByComment(comment);
            if (countByComment == 0) {
                commentRepository.delete(comment);
                return DeleteSubCommentMapper.INSTANCE.subCommentToDeleteSubCommentResponse(subComment.getId(),
                    comment.getId());
            }
        }
        return DeleteSubCommentMapper.INSTANCE.subCommentToDeleteSubCommentResponse(subComment.getId(), null);
    }
}
