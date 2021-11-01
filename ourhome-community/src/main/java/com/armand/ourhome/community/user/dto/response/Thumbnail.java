package com.armand.ourhome.community.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Builder
public class Thumbnail {
    private String mediaUrl;
    private Boolean isOnly;
}
