package com.armand.ourhome.community.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Builder
@JsonNaming
public class Thumbnail {
    private String mediaUrl;
    private Boolean isOnly;
}
