package com.armand.ourhome.community.follow.service;

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
import org.springframework.data.domain.Page;
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
            // 이렇게 사용자 id를 알려주는게 맞을까? 로깅할때만 써주면 될지도?
            throw new InvalidValueException(MessageFormat.format("이미 팔로우하고 있는 사용자입니다. (id : {})", followingId));
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
            throw new InvalidValueException(MessageFormat.format("이미 언팔로우한 사용자입니다. (id : {})", followingId));
        followRepository.deleteByFollowerAndFollowing(follower, following);
    }



    //    public PageableResponse<List<FeedResponse>> feedPage(Long myId, Pageable pageable) {
    public void feedPage(Long myId, CursorPageRequest pageRequest) {
        User me = userRepository.findById(myId).get();
        List<FeedResponse> pageContent = new ArrayList<>();

        // 내가 팔로잉하는 유저 아이디 리스트
        List<Long> userIdList = followRepository.findByFollowing(me).stream()
                .map(follow -> follow.getFollower().getId())
                .collect(Collectors.toList());

        Page<Post> postPage = postRepository.findByUserIdListOrderByIdDesc(
                userIdList,
                pageRequest.getLastId(),
                PageRequest.of(0, pageRequest.getSize())
        );

        for (Post post : postPage) {
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
            Long aLong = subCommentRepository.countByCommentList(commentList);

            System.out.println(commentList.size());
            System.out.println(aLong);




//            FeedResponse response = FeedResponse.builder()
//                    .profileImageUrl(user.getProfileImageUrl())
//                    .nickname(user.getNickname())
//                    .mediaUrlList(mediaUrlList)
//                    .description(contentList.get(0).getDescription())
//                    .tagList(tagList)
//                    .likeCount(likeRepository.countByPost(post))
//                    .isLike(likeRepository.existsByPostAndUser(post, me))
//                    .bookmarkCount(bookmarkRepository.countByPost(post))
//                    .isBookmark(bookmarkRepository.existsByPostAndUser(post, me))
//                    .commentCount()
//                    .build();


        }

    }

}
