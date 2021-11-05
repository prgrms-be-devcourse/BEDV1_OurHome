package com.armand.ourhome.community.post.entity;

import com.armand.ourhome.community.post.controller.common.CriteriaType;

import java.util.Arrays;

public enum ResidentialType implements Criteria {
    OFFICETEL("오피스텔&원룸"),
    APARTMENT("아파트"),
    VILLA("빌라"),
    DETACHED_HOUCE("단독주택"),
    OFFICE_SPACE("사무공간"),
    COMMERCIAL_SPACE("상업공간"),
    ETC("기타");


    private String displaceName;

    ResidentialType(String displaceName) {
        this.displaceName = displaceName;
    }

    public static Boolean getIfPresent(String residentialTypeRequest){
        return Arrays.stream(ResidentialType.values()).anyMatch((v) -> v.name().equals(residentialTypeRequest));
    }

    public String getDisplaceName(){
        return displaceName;
    }
}
