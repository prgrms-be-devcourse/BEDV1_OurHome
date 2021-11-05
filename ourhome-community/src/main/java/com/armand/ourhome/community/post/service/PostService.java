package com.armand.ourhome.community.post.service;

import com.armand.ourhome.common.utils.AwsS3Uploader;
import com.armand.ourhome.community.follow.repository.FollowRepository;
import com.armand.ourhome.community.post.controller.common.CriteriaType;
import com.armand.ourhome.community.post.dto.request.ReqContent;
import com.armand.ourhome.community.post.dto.request.ReqPost;
import com.armand.ourhome.community.post.dto.response.ResPost;
import com.armand.ourhome.community.post.entity.*;
import com.armand.ourhome.community.post.exception.CriteriaNotFountException;
import com.armand.ourhome.community.post.exception.PostNotFoundException;
import com.armand.ourhome.community.post.exception.UserNotFountException;
import com.armand.ourhome.community.post.mapper.PostMapper;
import com.armand.ourhome.community.post.repository.ContentRepository;
import com.armand.ourhome.community.post.repository.PostRepository;
import com.armand.ourhome.community.post.repository.TagRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toMap;

/**
 * Created by yunyun on 2021/10/29.
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ContentRepository contentRepository;
    private final TagRepository tagRepository;
    private final FollowRepository followRepository;
    private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);
    private final AwsS3Uploader awsS3Uploader;

    @Transactional
    public Long save(final ReqPost postDto){

        User user = userRepository.findById(postDto.getUserId()).orElseThrow(() -> new UserNotFountException("해당 사용자를 찾을 수 없습니다."));

        int contentSize = postDto.getContentList().size();
        for (int i = 0; i < contentSize; i++){
            String mediaUrl = awsS3Uploader.upload(postDto.getContentList().get(i).getImageBase64(), "post");
            postDto.getContentList().get(i).setMediaUrl(mediaUrl);
        }

        return postRepository.save(postMapper.toEntity(postDto, user)).getId();
    }

    public Page<ResPost> getAll(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFountException("해당 사용자를 찾을 수 없습니다."));
        Page<Post> postWithPage = postRepository.findAll(pageable);

        // 본 게시물에 대해 팔로워 여부 확인
        List<ResPost> postDtoList = setIsFollower(user, postWithPage.getContent(), postMapper.toDtoList(postWithPage.getContent()));

        return new PageImpl<>(postDtoList, pageable, postWithPage.getTotalElements());
    }


    public Page<ResPost> getAllBYCriteria(CriteriaType criteriaType, String type, Pageable pageable, Long userId){
        switch(criteriaType) {
            case RESIDENTIAL_TYPE -> {
                return getAllByResidentialType(ResidentialType.valueOf(type), pageable, userId);
            }
            case PLACE_TYPE -> {
                return getAllByPlaceType(PlaceType.valueOf(type), pageable, userId);
            }
            case TAG -> {
                return getAllByTag(type, pageable, userId);
            }
        }
         throw new CriteriaNotFountException(criteriaType.toString());
    }

    public Page<ResPost> getAllByResidentialType(ResidentialType residentialType, Pageable pageable, Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFountException("해당 사용자를 찾을 수 없습니다."));
        Page<Post> postWithPage = postRepository.findAllByResidentialType(residentialType, pageable);

        // 본 게시물에 대해 팔로워 여부 확인
        List<ResPost> postDtoList = setIsFollower(user, postWithPage.getContent(), postMapper.toDtoList(postWithPage.getContent()));
        return new PageImpl<>(postDtoList, pageable, postWithPage.getTotalElements());
    }

    public Page<ResPost> getAllByPlaceType(PlaceType placeType, Pageable pageable, Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFountException("해당 사용자를 찾을 수 없습니다."));
        Page<Post> postWithPage = postRepository.findAllByPlaceType(placeType, pageable);

        // 본 게시물에 대해 팔로워 여부 확인
        List<ResPost> postDtoList = setIsFollower(user, postWithPage.getContent(), postMapper.toDtoList(postWithPage.getContent()));
        return new PageImpl<>(postDtoList, pageable, postWithPage.getTotalElements());
    }

    public Page<ResPost> getAllByTag(String tagName, Pageable pageable, Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFountException("해당 사용자를 찾을 수 없습니다."));
        Page<Post> postWithPage = postRepository.findAllByTag(tagName, pageable);

        // 본 게시물에 대해 팔로워 여부 확인
        List<ResPost> postDtoList = setIsFollower(user, postWithPage.getContent(), postMapper.toDtoList(postWithPage.getContent()));
        return new PageImpl<>(postDtoList, pageable, postWithPage.getTotalElements());
    }

    @Transactional
    public Long update(final ReqPost postDto, Long postId){
        List<ReqContent> contentDtoList = postDto.getContentList();

        for (int i = 0; i < contentDtoList.size(); i++){
            if (contentDtoList.get(i).getUpdatedFlag()) {
                contentDtoList.get(i).setMediaUrl(awsS3Uploader.upload(contentDtoList.get(i).getImageBase64(), "post"));
            }
        }
        Post postBeforeUpdate = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString(), "post's id"));

        postMapper.updateFromDto(postDto, postBeforeUpdate);
        return postDto.getId();
    }

    @Transactional
    public ResPost getOne(final Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() ->new PostNotFoundException(postId.toString(), "post's id"));
        post.plusViewCount();
        return postMapper.toDto(post);
    }

    @Transactional
    public void delete(final Long postId){
        postRepository.deleteById(postId);
    }

    private List<ResPost> setIsFollower(User user, List<Post> posts, List<ResPost> resPosts){
        for (int i =0; i < posts.size(); i++){
            User userOfPost = posts.get(i).getUser();
            resPosts.get(i).setIsFollower(followRepository.existsByFollowerAndFollowing(user, userOfPost));
        }
        return resPosts;
    }
}
