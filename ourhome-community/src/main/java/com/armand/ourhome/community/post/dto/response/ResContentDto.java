package com.armand.ourhome.community.post.dto.response;

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
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResContentDto {
    @JsonProperty("id")
    private Long contentId;

    @NotBlank
    private String mediaUrl;
    private String description;
    @NotBlank
    private PlaceType placeType;
    private List<ResTagDto> tags;

    @Builder
    public ResContentDto(Long contentId,
                         String mediaUrl,
                         String description,
                         PlaceType placeType,
                         List<ResTagDto> tags){
        this.contentId = contentId;
        this.mediaUrl = mediaUrl;
        this.description = description;
        this.placeType = placeType;
        this.tags = tags;
    }
}
