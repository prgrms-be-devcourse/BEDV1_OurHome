package com.armand.ourhome.community.post.dto;

import com.armand.ourhome.community.post.entity.PlaceType;
import com.armand.ourhome.community.post.entity.Tag;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by yunyun on 2021/10/29.
 */

@Getter
public class ContentDto {
    private Long contentId;
    @NotBlank
    private String mediaUrl;
    private String description;
    @NotBlank
    private PlaceType placeType;
    private List<TagDto> tags;

    private Boolean updateFlag = false;
    public void setMediaUrl(String mediaUrl){
        this.mediaUrl = mediaUrl;
    }

    @Builder
    public ContentDto(Long contentId, String mediaUrl, String description, PlaceType placeType, List<TagDto> tags){
        this.contentId = contentId;
        this.mediaUrl = mediaUrl;
        this.description = description;
        this.placeType = placeType;
        this.tags = tags;
    }
}
