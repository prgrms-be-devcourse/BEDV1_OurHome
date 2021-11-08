package com.armand.ourhome.community.post.dto.request;

import com.armand.ourhome.community.post.entity.PlaceType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by yunyun on 2021/10/29.
 */

@Getter
public class CreateContentRequest {
    @JsonProperty("id")
    private Long contentId;

    @NotBlank
    private String mediaUrl = null;

    private String description;
    @NotBlank
    private PlaceType placeType;
    private List<CreateTagRequest> tags;

    public void setMediaUrl(String mediaUrl){
        this.mediaUrl = mediaUrl;
    }

    @JsonProperty("image_base64")
    private String imageBase64; // null

    @Builder
    public CreateContentRequest(Long contentId,
                                String description,
                                PlaceType placeType,
                                List<CreateTagRequest> tags,
                                String imageBase64){
        this.contentId = contentId;
        this.description = description;
        this.placeType = placeType;
        this.tags = tags;
        this.imageBase64 = imageBase64;
    }
}
