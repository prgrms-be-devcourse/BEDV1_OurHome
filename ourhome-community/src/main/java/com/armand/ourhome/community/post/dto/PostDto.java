package com.armand.ourhome.community.post.dto;

import com.armand.ourhome.community.post.entity.Content;
import com.armand.ourhome.community.post.entity.ResidentialType;
import com.armand.ourhome.community.post.entity.SquareType;
import com.armand.ourhome.community.post.entity.StyleType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by yunyun on 2021/10/29.
 */

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostDto {

    private Long id;
    @NotBlank
    @Size(min = 0, max = 150)
    private String title;
    private SquareType squareType;
    private ResidentialType residentialType;
    private StyleType styleType;

    private List<ContentDto> contentList;

    private Long userId; // session에서 맞는지 모든 게시물마다 정보를 받는지 알아봐야 함. 우선 이메일을 받는다는 가능으로 진행.

    private int viewCount;

    @Builder
    public PostDto(Long id, String title, SquareType squareType, ResidentialType residentialType, StyleType styleType, List<ContentDto> contentList, int viewCount, Long userId){
        this.id = id;
        this.title = title;
        this.squareType = squareType;
        this.residentialType = residentialType;
        this.styleType = styleType;
        this.contentList = contentList;
        this.viewCount = viewCount;
        this.userId = userId;
    }
}
