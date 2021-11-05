package com.armand.ourhome.community.post.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by yunyun on 2021/11/04.
 */

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResReturnId {
    private Long id;
}
