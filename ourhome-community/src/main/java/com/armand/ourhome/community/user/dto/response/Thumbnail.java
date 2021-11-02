package com.armand.ourhome.community.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Builder
public class Thumbnail {
    @JsonProperty("media_url")
    private String mediaUrl;

    @JsonProperty("is_only")
    private Boolean isOnly;
}
