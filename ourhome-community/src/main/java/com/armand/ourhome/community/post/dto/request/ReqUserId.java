package com.armand.ourhome.community.post.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by yunyun on 2021/11/05.
 */

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReqUserId {

    @NotNull
    private Long userId;

    @JsonCreator
    public ReqUserId(Long userId){
        this.userId = userId;
    }
}
