package com.armand.ourhome.community.post.mapper;

import com.armand.ourhome.community.post.dto.PostDto;
import com.armand.ourhome.community.post.entity.Post;
import com.armand.ourhome.domain.user.User;
import org.mapstruct.*;

/**
 * Created by yunyun on 2021/10/29.
 */

@Mapper
public interface PostMapper {

    @Mapping(source="user", target = "user")
    Post toEntity(PostDto postDto, User user);

    @Mapping(target = "userId", ignore = true)
    PostDto toDto(Post post);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(PostDto postDto, @MappingTarget Post post);
}
