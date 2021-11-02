package com.armand.ourhome.community.post.entity;

public enum ResidentialType {
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

    public String getDisplaceName(){
        return displaceName;
    }
}
