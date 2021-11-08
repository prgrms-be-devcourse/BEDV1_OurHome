package com.armand.ourhome.community.post.mapper;

import com.armand.ourhome.common.utils.AwsS3Uploader;
import com.armand.ourhome.community.post.dto.request.ReqContent;
import com.armand.ourhome.community.post.dto.request.ReqPost;
import com.armand.ourhome.community.post.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created by yunyun on 2021/11/07.
 */

@Component
@RequiredArgsConstructor
public class PostConverter {

    final AwsS3Uploader awsS3Uploader;

    public void updateConverter(ReqPost reqPostDto, Post postEntity){

        postEntity.updatePost(reqPostDto.getTitle(),
                reqPostDto.getSquareType(),
                reqPostDto.getResidentialType(),
                reqPostDto.getStyleType()
        );

        String mediaUrl;
        ReqContent reqContent;
        Content contentEntity;
        boolean isUpdatedForMedia;

        // update post
        if(reqPostDto.getContentList().size() * postEntity.getContentList().size()> 0
                && reqPostDto.getContentList().size() == postEntity.getContentList().size()) {

            for (int i = 0; i < reqPostDto.getContentList().size(); i++){

                reqContent = reqPostDto.getContentList().get(i);
                contentEntity = postEntity.getContentList().get(i);

                // update content
                if (reqContent.getImageBase64() == null){ // update 하지 않을 때, null 값으로 보내주기로 했음.
                    isUpdatedForMedia = false;
                    mediaUrl = "";
                } else {
                    isUpdatedForMedia = true;
                    mediaUrl = awsS3Uploader.upload(reqContent.getImageBase64(), "post");
                }
                postEntity.getContentList().get(i).updateContent(mediaUrl,
                        reqContent.getDescription(),
                        reqContent.getPlaceType(),
                        isUpdatedForMedia
                        );

                    // update tag
                    if(reqContent.getTags().size() * contentEntity.getTags().size() > 0
                            && reqContent.getTags().size() == contentEntity.getTags().size()) {

                        for (int j = 0; j < reqContent.getTags().size(); j++){
                            contentEntity.getTags().get(j).updateTag(reqContent.getTags().get(j).getName());
                        }
                    }
            }

        }
    }
}
