package com.armand.ourhome.community.post.entity;

import java.util.Arrays;

public enum PlaceType implements Criteria {
    ONEROOM("원룸"),
    LIVINGROOM("거실"),
    KITCHEN("주방"),
    BATHROOM("욕실"),
    DRESSROOM("드레스룸"),
    VERANDA("베란다"),
    OFFICEROOM("사무공간"),
    FURNITURE("가구&소품"),
    EXTERIOR("외관&기타");


    private String displaceName;

    PlaceType(String displaceName) {
        this.displaceName = displaceName;
    }

    public static String getTypeNames(){
        StringBuffer typeNames = null;
        Arrays.stream(PlaceType.values()).toList().forEach(typeName -> typeNames.append(typeName+ " "));
        return typeNames.toString();
    }

    public static Boolean exists(String placeTypeRequest){
        return Arrays.stream(PlaceType.values()).anyMatch((v) -> v.name().equals(placeTypeRequest));
    }
    public String getDisplaceName(){
        return displaceName;
    }

}
