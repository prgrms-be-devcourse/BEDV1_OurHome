package com.armand.ourhome.community.post.mapper;

import com.armand.ourhome.community.post.dto.request.ReqPostDto;
import com.armand.ourhome.community.post.dto.response.ResPostDto;
import com.armand.ourhome.community.post.entity.Post;
import com.armand.ourhome.domain.user.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yunyun on 2021/10/29.
 */

@Mapper
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source="user", target = "user")
    @Mapping(source = "reqPostDto.id", target = "id")
    Post toEntity(ReqPostDto reqPostDto, User user);

    @Mapping(target = "userId", ignore = true)
    ResPostDto toDto(Post post);

    @Mapping(target = "userId", ignore = true)
    List<ResPostDto> toDtoList(List<Post> post);


    @Transactional
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(ReqPostDto reqPostDto,  @MappingTarget Post post);
}
