package com.armand.ourhome.community.post.service;

import com.armand.ourhome.common.utils.AwsS3Uploader;
import com.armand.ourhome.community.post.dto.request.ReqContentDto;
import com.armand.ourhome.community.post.dto.request.ReqPostDto;
import com.armand.ourhome.community.post.dto.response.ResPostDto;
import com.armand.ourhome.community.post.entity.*;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

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
    private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);
    private final AwsS3Uploader awsS3Uploader;

    @Transactional
    public Long save(final ReqPostDto postDto){

        int contentSize = postDto.getContentList().size();
        for (int i = 0; i < contentSize; i++){
            String mediaUrl = awsS3Uploader.upload(postDto.getContentList().get(i).getImageBase64(), "user-posts");
            postDto.getContentList().get(i).setMediaUrl(mediaUrl);
        }
        User user = userRepository.findById(postDto.getUserId()).orElseThrow(() -> new BusinessException("해당 사용자 정보는 존재하지 않습니다.", ErrorCode.ENTITY_NOT_FOUND));
        return postRepository.save(postMapper.toEntity(postDto, user)).getId();
    }


    public Page<PostDto> getAll(Pageable pageable) {
        Page<Post> postWithPage = postRepository.findAll(pageable);
        List<ResPostDto> postDtoList = postMapper.toDtoList(postWithPage.getContent());
        return new PageImpl<>(postDtoList, pageable, postWithPage.getTotalElements());
    }

    public Page<ResPostDto> getAllByResidentialType(ResidentialType residentialType, Pageable pageable){
        Page<Post> postWithPage = postRepository.findAllByResidentialType(residentialType, pageable);
        List<ResPostDto> postDtoList = postMapper.toDtoList(postWithPage.getContent());
        return new PageImpl<>(postDtoList, pageable, postWithPage.getTotalElements());
    }


    public Page<PostDto> getAllByPlaceType(PlaceType placeType, Pageable pageable){ // 중복 post 제거 필요
        Page<Content> contentWithPage = contentRepository.findAllByPlaceType(placeType, pageable);
        List<ResPostDto> contentDtoList = postMapper.toDtoList(contentWithPage.getContent().stream().map(v -> v.getPost()).toList());
        return new PageImpl<>(contentDtoList, pageable, contentWithPage.getTotalElements());
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public Page<ResPostDto> getAllByTag(String tagName, Pageable pageable){ // 중복 post 제거 필요
        Page<Tag> tagWithPage = tagRepository.findAllByName(tagName, pageable);
        List<ResPostDto> tagDtoList = postMapper.toDtoList(tagWithPage.getContent().stream().map(v -> v.getContent().getPost()).toList());
        return new PageImpl<>(tagDtoList, pageable, tagWithPage.getTotalElements());
    }


    @Transactional
    public Long update(final ReqPostDto postDto, Long postId){
        List<ReqContentDto> contentDtoList = postDto.getContentList();

        for (int i = 0; i < contentDtoList.size(); i++){
            if (contentDtoList.get(i).getUpdatedFlag()) {
                contentDtoList.get(i).setMediaUrl(awsS3Uploader.upload(contentDtoList.get(i).getImageBase64(), "user-posts"));
            }
        }
        Post postBeforeUpdate = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString(), "post_id"));
        postMapper.updateFromDto(postDto, postBeforeUpdate);
        return postDto.getId();
    }



    @Transactional
    public ResPostDto getOne(final Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() ->new PostNotFoundException(postId.toString(), "post_id"));
        post.plusViewCount();
        return postMapper.toDto(post);
    }

    @Transactional
    public void delete(final Long postId){
        postRepository.deleteById(postId);
    }
}
