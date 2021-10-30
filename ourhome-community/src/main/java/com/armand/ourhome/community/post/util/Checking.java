package com.armand.ourhome.community.post.util;

/**
 * Created by yunyun on 2021/10/30.
 */
public class Checking {
    public static void validLength(int min, int max, String targetFieldName, String target){
        int length = target.length();
        if (length <= min) throw new IllegalArgumentException("{}은(는) {}초과의 자리수만을 허용합니다.".formatted(targetFieldName, min));
        if (max < length) throw new IllegalArgumentException("{}은(는) {}미만의 자리수만을 허용합니다.".formatted(targetFieldName, min));
    }
}
