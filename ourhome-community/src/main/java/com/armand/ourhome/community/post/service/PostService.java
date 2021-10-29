package com.armand.ourhome.community.post.service;

import com.armand.ourhome.common.error.exception.BusinessException;
import com.armand.ourhome.common.error.exception.ErrorCode;
import com.armand.ourhome.community.post.dto.PostDto;
import com.armand.ourhome.community.post.entity.Post;
import com.armand.ourhome.community.post.mapper.PostMapper;
import com.armand.ourhome.community.post.repository.PostRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;

/**
 * Created by yunyun on 2021/10/29.
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);

    @Transactional
    public Long save(final PostDto postDto, List<String> mediaUrl){
        int contentSize = postDto.getContentList().size();
        for (int i = 0; i < contentSize; i++){
            postDto.getContentList().get(i).setMediaUrl(mediaUrl.get(i));
        }
        User user = userRepository.findById(postDto.getUserId()).orElseThrow(() -> new BusinessException("해당 사용자 정보는 존재하지 않습니다.", ErrorCode.ENTITY_NOT_FOUND));
        return postRepository.save(postMapper.toEntity(postDto, user)).getPostId();
    }

    public List<PostDto> getAll(Pageable pageable) {
        return null;
    }

    @Transactional
    public Long update(final PostDto postDto, List<String> mediaUrl){
        int contentSize = postDto.getContentList().size();
        for (int i = 0; i < contentSize; i++){

            postDto.getContentList().get(i).setMediaUrl(mediaUrl.get(i));
        }
        Post postBeforeUpdate = postRepository.findById(postDto.getPostId()).orElseThrow(() -> new BusinessException("해당 게시물은 존재하지 않습니다.", ErrorCode.ENTITY_NOT_FOUND));
        //User user = userRepository.findById(postDto.getUserId()).orElseThrow(() -> new BusinessException("해당 사용자 정보는 존재하지 않습니다.", ErrorCode.ENTITY_NOT_FOUND));
        postMapper.updateFromDto(postDto, postBeforeUpdate);
        return postDto.getPostId();
    }

    @Transactional
    public PostDto getOne(final Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException("해당 게시물은 존재하지 않습니다.", ErrorCode.ENTITY_NOT_FOUND));
        post.plusViewCount();
        return postMapper.toDto(post);
    }

    @Transactional
    public void delete(final Long postId){
        postRepository.deleteById(postId);
    }
}
