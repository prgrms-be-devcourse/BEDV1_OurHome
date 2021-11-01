package com.armand.ourhome.community.post.dto;

import com.armand.ourhome.community.post.entity.PlaceType;
import com.armand.ourhome.community.post.entity.Tag;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public void setMediaUrl(String mediaUrl){
        this.mediaUrl = mediaUrl;
    }

    @JsonProperty("image_base64")
    private String imageBase64;
    private Boolean updatedFlag = false;

    @Builder
    public ContentDto(Long contentId, String mediaUrl, String description, PlaceType placeType, List<TagDto> tags, String imageBase64, Boolean updatedFlag){
        this.contentId = contentId;
        this.mediaUrl = mediaUrl;
        this.description = description;
        this.placeType = placeType;
        this.tags = tags;
        this.imageBase64 = imageBase64;
        this.updatedFlag = updatedFlag;
    }
}
