package com.armand.ourhome.community.follow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CursorPageRequest {
    private int size;
    private Long lastId;
}
