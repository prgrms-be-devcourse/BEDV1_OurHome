package com.armand.ourhome.community.post.service;

import com.armand.ourhome.common.api.PageResponse;
import com.armand.ourhome.common.utils.AwsS3Uploader;
import com.armand.ourhome.community.follow.repository.FollowRepository;
import com.armand.ourhome.community.post.controller.common.CriteriaType;
import com.armand.ourhome.community.post.dto.request.CreatePostRequest;
import com.armand.ourhome.community.post.dto.request.UpdatePostRequest;
import com.armand.ourhome.community.post.dto.response.ResPost;
import com.armand.ourhome.community.post.entity.*;
import com.armand.ourhome.community.post.exception.CriteriaNotFountException;
import com.armand.ourhome.community.post.exception.PostNotFoundException;
import com.armand.ourhome.community.post.exception.UserNotFountException;
import com.armand.ourhome.community.post.mapper.PostConverter;
import com.armand.ourhome.community.post.mapper.PostMapper;
import com.armand.ourhome.community.post.repository.PostRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
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
    private final FollowRepository followRepository;
    private final PostConverter postConverter;
    private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);
    private final AwsS3Uploader awsS3Uploader;




    @Transactional
    public Long save(final CreatePostRequest postDto){

        User user = userRepository.findById(postDto.getUserId()).orElseThrow(() -> new UserNotFountException("해당 사용자를 찾을 수 없습니다."));
        postDto.getContentList().forEach(content -> content.setMediaUrl(awsS3Uploader.upload(content.getImageBase64(),"post")));

        return postRepository.save(postMapper.toEntity(postDto, user)).getId();
    }

    public PageResponse<List<ResPost>> getAll(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFountException("해당 사용자를 찾을 수 없습니다."));
        Page<Post> postWithPage = postRepository.findAll(pageable);

        // 본 게시물에 대해 팔로워 여부 확인
        List<ResPost> postDtoList = isFollowing(user, postWithPage.getContent(), postMapper.toDtoList(postWithPage.getContent()));

        return new PageResponse<List<ResPost>>(postDtoList, postWithPage.getNumber(), postWithPage.getTotalPages(), postWithPage.getNumberOfElements(), postWithPage.getTotalElements());
    }


    public PageResponse<List<ResPost>> getAllByCriteria(CriteriaType criteriaType, String type, Pageable pageable, Long userId){
//        CriteriaType criteriaType = CriteriaType.(criteriaTypeRequest);
        switch(criteriaType) {
            case RESIDENTIAL_TYPE -> {
                if (! ResidentialType.exists(type)) throw new CriteriaNotFountException(type);
                return getAllByResidentialType(ResidentialType.valueOf(type), pageable, userId);
            }
            case PLACE_TYPE -> {
                if (! PlaceType.exists(type)) throw new CriteriaNotFountException(type);
                return getAllByPlaceType(PlaceType.valueOf(type), pageable, userId);
            }
            case TAG -> {
                return getAllByTag(type, pageable, userId);
            }
        }
         throw new CriteriaNotFountException(criteriaType.toString());
    }

    public PageResponse<List<ResPost>> getAllByResidentialType(ResidentialType residentialType, Pageable pageable, Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFountException("해당 사용자를 찾을 수 없습니다."));
        Page<Post> postWithPage = postRepository.findAllByResidentialType(residentialType, pageable);

        // 본 게시물에 대해 팔로워 여부 확인
        List<ResPost> postDtoList = isFollowing(user, postWithPage.getContent(), postMapper.toDtoList(postWithPage.getContent()));

        return new PageResponse<List<ResPost>>(postDtoList, postWithPage.getNumber(), postWithPage.getTotalPages(), postWithPage.getNumberOfElements(), postWithPage.getTotalElements());
    }

    public PageResponse<List<ResPost>> getAllByPlaceType(PlaceType placeType, Pageable pageable, Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFountException("해당 사용자를 찾을 수 없습니다."));
        Page<Post> postWithPage = postRepository.findAllByPlaceType(placeType, pageable);

        // 본 게시물에 대해 팔로워 여부 확인
        List<ResPost> postDtoList = isFollowing(user, postWithPage.getContent(), postMapper.toDtoList(postWithPage.getContent()));

        return new PageResponse<List<ResPost>>(postDtoList, postWithPage.getNumber(), postWithPage.getTotalPages(), postWithPage.getNumberOfElements(), postWithPage.getTotalElements());
    }

    public PageResponse<List<ResPost>> getAllByTag(String tagName, Pageable pageable, Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFountException("해당 사용자를 찾을 수 없습니다."));
        Page<Post> postWithPage = postRepository.findAllByTag(tagName, pageable);

        // 본 게시물에 대해 팔로워 여부 확인
        List<ResPost> postDtoList = isFollowing(user, postWithPage.getContent(), postMapper.toDtoList(postWithPage.getContent()));

        return new PageResponse<List<ResPost>>(postDtoList, postWithPage.getNumber(), postWithPage.getTotalPages(), postWithPage.getNumberOfElements(), postWithPage.getTotalElements());
    }

    @Transactional
    public Long update(final UpdatePostRequest postDto, Long postId){
        Post postBeforeUpdate = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString(), "post's id"));
        postConverter.updateConverter(postDto, postBeforeUpdate);
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

    private List<ResPost> isFollowing(User user, List<Post> posts, List<ResPost> resPosts){
        for (int i =0; i < posts.size(); i++){
            User writer = posts.get(i).getUser();
            resPosts.get(i).setIsFollower(followRepository.existsByFollowerAndFollowing(user, writer));
        }
        return resPosts;
    }
}
