package com.armand.ourhome.community.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Thumbnail {
    private String mediaUrl;
    private Boolean isOnly;
}
