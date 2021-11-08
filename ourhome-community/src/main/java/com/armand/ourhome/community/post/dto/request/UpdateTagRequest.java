package com.armand.ourhome.community.post.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

/**
 * Created by yunyun on 2021/10/29.
 */

@Getter
public class UpdateTagRequest {
    @JsonProperty("id")
    private Long tagId;

    @NotBlank
    @Max(30)
    private String name;

    @Builder
    public UpdateTagRequest(Long tagId, String name){
        this.tagId = tagId;
        this.name = name;
    }
}
