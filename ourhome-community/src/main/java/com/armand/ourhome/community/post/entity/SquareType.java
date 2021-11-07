package com.armand.ourhome.community.post.entity;

import java.util.Arrays;

public enum SquareType implements Criteria {
    SIZE_LESS_THAN_10_PYEONG("10평미만"),
    SIZE_10_PYEONG("10평대"),
    SIZE_20_PYEONG("20평대"),
    SIZE_30_PYEONG("30평대"),
    SIZE_40_PYEONG("40평대"),
    SIZE_MORE_THAN_50_PYEONG("50평이상");


    private String displaceName;

    SquareType(String displaceName) {
        this.displaceName = displaceName;
    }

    public String getTypeNames(){
        StringBuffer typeNames = null;
        Arrays.stream(SquareType.values()).toList().forEach(typeName -> typeNames.append(typeName+ " "));
        return typeNames.toString();
    }

    public String getDisplaceName(){
        return displaceName;
    }
}
