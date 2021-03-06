package com.armand.ourhome.community.user.service;


import com.armand.ourhome.common.api.CursorPageRequest;
import com.armand.ourhome.common.api.CursorPageResponse;
import com.armand.ourhome.common.error.exception.EntityNotFoundException;
import com.armand.ourhome.common.error.exception.InvalidValueException;
import com.armand.ourhome.common.error.exception.user.UserNotFoundException;
import com.armand.ourhome.common.utils.AwsS3Uploader;
import com.armand.ourhome.community.bookmark.repository.BookmarkRepository;
import com.armand.ourhome.community.follow.entity.Follow;
import com.armand.ourhome.community.post.entity.*;
import com.armand.ourhome.community.user.dto.response.FollowInfoResponse;
import com.armand.ourhome.community.follow.repository.FollowRepository;
import com.armand.ourhome.community.like.repository.LikeRepository;
import com.armand.ourhome.community.post.repository.PostRepository;
import com.armand.ourhome.community.user.dto.mapper.SignUpMapper;
import com.armand.ourhome.community.user.dto.request.LoginRequest;
import com.armand.ourhome.community.user.dto.request.SignUpRequest;
import com.armand.ourhome.community.user.dto.request.UpdateInfoRequest;
import com.armand.ourhome.community.user.dto.request.UpdatePasswordRequest;
import com.armand.ourhome.community.user.dto.response.*;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final BookmarkRepository bookmarkRepository;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    private final AwsS3Uploader awsS3Uploader;
    private final SignUpMapper signUpMapper = Mappers.getMapper(SignUpMapper.class);

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        validDuplicateEmail(email);
        String nickname = signUpRequest.getNickname();
        validDuplicateNickname(nickname);
        // request -> entity -> response
        User user = signUpMapper.requestToEntity(signUpRequest);
        User save = userRepository.save(user);
        return signUpMapper.entityToResponse(save);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Optional<User> user = userRepository.findByEmail(email);
        // ????????? email ??????
        if (user.isEmpty())
            throw new EntityNotFoundException("???????????? ?????? ??????????????????.");
        // ?????? ??????
        String foundPassword = user.get().getPassword();
        if (!password.equals(foundPassword))
            throw new InvalidValueException("??????????????? ????????????");
        return LoginResponse.of(user.get());
    }

    @Transactional
    public UpdateResponse updateInfo(Long myId, UpdateInfoRequest updateInfoRequest) {
        String nickname = updateInfoRequest.getNickname();
        validDuplicateNickname(nickname);
        String description = updateInfoRequest.getDescription();
        String profileImageBase64 = updateInfoRequest.getProfileImageBase64();
        String profileImageUrl = awsS3Uploader.upload(profileImageBase64, "profile");

        User me = userRepository.findById(myId).get();
        me.updateInfo(nickname, description, profileImageUrl);

        // update??? ????????? ???????????? ?????? flush
        userRepository.flush();
        return UpdateResponse.of(me);
    }

    @Transactional
    public UpdateResponse updatePassword(Long myId, UpdatePasswordRequest updatePasswordRequest) {
        String password = updatePasswordRequest.getPassword();
        User me = userRepository.findById(myId).get();
        me.updatePassword(password);
        userRepository.flush();
        return UpdateResponse.of(me);
    }

    public UserInfoResponse userPage(Long id, Long myId, Pageable pageable) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty())
            throw new UserNotFoundException(id);
        User user = byId.get();
        // Thumnail list ??????
        Page<Post> postList = postRepository.findAllByUser(user, pageable);
        List<Thumbnail> thumbnailList = new ArrayList<>();
        for (Post post : postList) {
            List<Content> contentList = post.getContentList();
            thumbnailList.add(
                    Thumbnail.builder()
                            .mediaUrl(contentList.get(0).getMediaUrl())
                            .isOnly(contentList.size() == 1)
                            .build()
            );
        }
        // ?????? response ??????
        UserInfoResponse.UserInfoResponseBuilder responseBuilder = UserInfoResponse.builder();
        responseBuilder
                .nickname(user.getNickname())
                .description(user.getDescription())
                .profileImageUrl(user.getProfileImageUrl())
                .followerCount(followRepository.countByFollower(user))
                .followingCount(followRepository.countByFollowing(user))
                .postCount(postRepository.countAllByUser(user))
                .thumbnailList(thumbnailList);
        // ?????????????????? ?????? ?????????, ????????? ?????? ???????????????
        if (Objects.equals(id, myId)) {
            responseBuilder
                    .bookmarkCount(bookmarkRepository.countByUser(user))
                    .likeCount(likeRepository.countByUser(user));
        }
        // ????????????????????? ?????? ????????? ????????? ??????
        else {
            User me = userRepository.findById(myId).get();   // ?????? ?????????
            responseBuilder
                    .isFollowing(followRepository.existsByFollowerAndFollowing(me, user));
        }
        return responseBuilder.build();
    }

    public CursorPageResponse<List<FollowInfoResponse>> followingPage(Long id, Long myId, CursorPageRequest pageRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        User me = userRepository.findById(myId).get();

        PageRequest pageable = PageRequest.of(0, pageRequest.getSize());
        List<Follow> followList = pageRequest.getIsFirst() ?
                followRepository.findByFollowerOrderByIdDesc(user, pageable) :
                followRepository.findByFollowerAndIdLessThanOrderByIdDesc(user, pageRequest.getLastId(), pageable);

        List<FollowInfoResponse> pageContent = new ArrayList<>();
        for (Follow follow : followList) {
            pageContent.add(
                    FollowInfoResponse.of(
                            follow.getFollowing(),
                            followRepository.existsByFollowerAndFollowing(me, follow.getFollowing())
                    )
            );
        }
        Long lastId = followList.isEmpty() ? null : followList.get(followList.size() - 1).getId();
        return new CursorPageResponse<>(pageContent, lastId);
    }

    public CursorPageResponse<List<FollowInfoResponse>> followerPage(Long id, Long myId, CursorPageRequest pageRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        User me = userRepository.findById(myId).get();

        PageRequest pageable = PageRequest.of(0, pageRequest.getSize());
        List<Follow> followList = pageRequest.getIsFirst() ?
                followRepository.findByFollowingOrderByIdDesc(user, pageable) :
                followRepository.findByFollowingAndIdLessThanOrderByIdDesc(user, pageRequest.getLastId(), pageable);

        List<FollowInfoResponse> pageContent = new ArrayList<>();
        for (Follow follow : followList) {
            pageContent.add(
                    FollowInfoResponse.of(
                            follow.getFollower(),
                            followRepository.existsByFollowerAndFollowing(me, follow.getFollower())
                    )
            );
        }
        Long lastId = followList.isEmpty() ? null : followList.get(followList.size() - 1).getId();
        return new CursorPageResponse<>(pageContent, lastId);
    }

    // ---------------------------- ( util ) ----------------------------

    private void validDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email))
            throw new InvalidValueException("?????? ???????????? ???????????? ????????????");
    }

    private void validDuplicateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname))
            throw new InvalidValueException("?????? ???????????? ???????????? ????????????");
    }

}
