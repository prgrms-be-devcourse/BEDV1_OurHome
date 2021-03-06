package com.armand.ourhome.community.follow.service;

import com.armand.ourhome.common.api.CursorPageResponse;
import com.armand.ourhome.common.api.PageResponse;
import com.armand.ourhome.common.error.exception.InvalidValueException;
import com.armand.ourhome.common.error.exception.user.UserNotFoundException;
import com.armand.ourhome.community.bookmark.repository.BookmarkRepository;
import com.armand.ourhome.community.comment.entity.Comment;
import com.armand.ourhome.community.comment.repository.CommentRepository;
import com.armand.ourhome.common.api.CursorPageRequest;
import com.armand.ourhome.community.follow.dto.FeedResponse;
import com.armand.ourhome.community.follow.entity.Follow;
import com.armand.ourhome.community.follow.repository.FollowRepository;
import com.armand.ourhome.community.like.repository.LikeRepository;
import com.armand.ourhome.community.post.entity.Content;
import com.armand.ourhome.community.post.entity.Post;
import com.armand.ourhome.community.post.repository.PostRepository;
import com.armand.ourhome.community.sub_comment.repository.SubCommentRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;

    @Transactional
    public void follow(Long followingId, Long myId) {
        User follower = userRepository.findById(myId).get();
        User following = userRepository.findById(followingId).orElseThrow(() -> new UserNotFoundException(followingId));
        if (followRepository.existsByFollowerAndFollowing(follower, following))
            // ????????? ????????? id??? ??????????????? ?????????? ??????????????? ????????? ??????????
            throw new InvalidValueException(MessageFormat.format("?????? ??????????????? ?????? ??????????????????. (id : {})", followingId));
        followRepository.save(
                Follow.builder()
                        .follower(follower)
                        .following(following)
                        .build()
        );
    }

    @Transactional
    public void unfollow(Long followingId, Long myId) {
        User follower = userRepository.findById(myId).get();
        User following = userRepository.findById(followingId).orElseThrow(() -> new UserNotFoundException(followingId));
        if (!followRepository.existsByFollowerAndFollowing(follower, following))
            throw new InvalidValueException(MessageFormat.format("?????? ??????????????? ??????????????????. (id : {})", followingId));
        followRepository.deleteByFollowerAndFollowing(follower, following);
    }

    public CursorPageResponse<List<FeedResponse>> feedPage(Long myId, CursorPageRequest pageRequest) {
        User me = userRepository.findById(myId).get();
        List<Long> userIdList = followRepository.findByFollowing(me).stream()
                .map(follow -> follow.getFollower().getId())
                .collect(Collectors.toList());

        PageRequest pageable = PageRequest.of(0, pageRequest.getSize());
        List<Post> postList = pageRequest.getIsFirst() ?
                postRepository.findByUserIdListOrderByIdDesc(userIdList, pageable) :
                postRepository.findByUserIdListAndIdLessThanOrderByIdDesc(userIdList, pageRequest.getLastId(), pageable);

        List<FeedResponse> pageContent = new ArrayList<>();
        for (Post post : postList) {
            User user = post.getUser();

            List<Content> contentList = post.getContentList();
            List<String> mediaUrlList = contentList.stream()
                    .map(content -> content.getMediaUrl())
                    .collect(Collectors.toList());

            List<String> tagList = contentList.stream()
                    .map(content -> content.getTags())
                    .flatMap(tags -> tags.stream())
                    .map(tag -> tag.getName())
                    .collect(Collectors.toList());

            List<Comment> commentList = commentRepository.findAllByPost(post);
            Long allCommentCount = commentList.size() + subCommentRepository.countByCommentList(commentList);

            pageContent.add(
                    FeedResponse.builder()
                            .userId(user.getId())
                            .profileImageUrl(user.getProfileImageUrl())
                            .nickname(user.getNickname())
                            .postId(post.getId())
                            .createdAt(post.getCreatedAt())
                            .mediaUrlList(mediaUrlList)
                            .description(contentList.get(0).getDescription())
                            .tagList(tagList)
                            .likeCount(likeRepository.countByPost(post))
                            .isLike(likeRepository.existsByPostAndUser(post, me))
                            .bookmarkCount(bookmarkRepository.countByPost(post))
                            .isBookmark(bookmarkRepository.existsByPostAndUser(post, me))
                            .allCommentCount(allCommentCount)
                            .build()
            );
        }

        Long lastId = postList.isEmpty() ? null : postList.get(postList.size() - 1).getId();
        return new CursorPageResponse<>(pageContent, lastId);
    }

}
