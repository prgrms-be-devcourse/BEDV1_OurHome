package com.armand.ourhome.community.post.entity;

import java.util.Arrays;

public enum StyleType implements Criteria {
    MODERN("모던"),
    NORDIC_STYPE("북유럽"),
    VINTAGE("빈티지"),
    NATURAL("네추럴"),
    ASIAN_STYPE("한국&아시아"),
    UNIQUE("유니크");


    private String displaceName;

    StyleType(String displaceName) {
        this.displaceName = displaceName;
    }

    public String getTypeNames(){
        StringBuffer typeNames = null;
        Arrays.stream(StyleType.values()).toList().forEach(typeName -> typeNames.append(typeName+ " "));
        return typeNames.toString();
    }

    public String getDisplaceName(){
        return displaceName;
    }
}
