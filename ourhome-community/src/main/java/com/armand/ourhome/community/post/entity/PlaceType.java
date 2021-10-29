package com.armand.ourhome.community.post.entity;

public enum PlaceType {
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

    public String getDisplaceName(){
        return displaceName;
    }

}
