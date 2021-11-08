package com.armand.ourhome.community.post.dto.request;

import com.armand.ourhome.community.post.entity.ResidentialType;
import com.armand.ourhome.community.post.entity.SquareType;
import com.armand.ourhome.community.post.entity.StyleType;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by yunyun on 2021/10/29.
 */

@Getter
public class UpdatePostRequest {

    private Long id;
    @NotBlank
    @Size(min = 0, max = 150)
    private String title;
    private SquareType squareType;
    private ResidentialType residentialType;
    private StyleType styleType;

    private List<UpdateContentRequest> contentList;

    private Long userId;

    private int viewCount;

    @Builder
    public UpdatePostRequest(Long id, String title, SquareType squareType, ResidentialType residentialType, StyleType styleType, List<UpdateContentRequest> contentList, int viewCount, Long userId){
        this.id = id;
        this.title = title;
        this.squareType = squareType;
        this.residentialType = residentialType;
        this.styleType = styleType;
        this.contentList = contentList;
        this.viewCount = viewCount;
        this.userId = userId;
    }
}
