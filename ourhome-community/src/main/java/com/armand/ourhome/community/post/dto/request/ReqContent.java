package com.armand.ourhome.community.post.dto.request;

import com.armand.ourhome.community.post.entity.PlaceType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by yunyun on 2021/10/29.
 */

@Getter
public class ReqContent {
    @JsonProperty("id")
    private Long contentId;

    @NotBlank
    private String mediaUrl = null;

    private String description;
    @NotBlank
    private PlaceType placeType;
    private List<ReqTag> tags;

    public void setMediaUrl(String mediaUrl){
        this.mediaUrl = mediaUrl;
    }

    @JsonProperty("image_base64")
    private String imageBase64; // null

//    private Boolean updatedFlag = false; // 필드 삭제

    @Builder
    public ReqContent(Long contentId,
                      String mediaUrl,
                      String description,
                      PlaceType placeType,
                      List<ReqTag> tags,
                      String imageBase64,
                      Boolean updatedFlag){
        this.contentId = contentId;
//        this.mediaUrl = mediaUrl;
        this.description = description;
        this.placeType = placeType;
        this.tags = tags;
        this.imageBase64 = imageBase64;
//        this.updatedFlag = updatedFlag;
    }
}
