package com.armand.ourhome.community.post.mapper;

import com.armand.ourhome.community.post.dto.PostDto;
import com.armand.ourhome.community.post.entity.Post;
import com.armand.ourhome.domain.user.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yunyun on 2021/10/29.
 */

@Mapper
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source="user", target = "user")
    Post toEntity(PostDto postDto, User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "contentDto.imageBase64", ignore = true)
    @Mapping(target = "contentDto.updatedFlag", ignore = true)
    PostDto toDto(Post post);

    @Mapping(target = "userId", ignore = true)
    List<PostDto> toDtoList(List<Post> post);

    @Transactional
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(PostDto postDto,  @MappingTarget Post post);
}
