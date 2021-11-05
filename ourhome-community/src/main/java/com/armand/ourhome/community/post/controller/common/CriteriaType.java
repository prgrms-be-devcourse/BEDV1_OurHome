package com.armand.ourhome.community.post.controller.common;

import com.armand.ourhome.community.post.exception.CriteriaNotFountException;
import org.yaml.snakeyaml.util.EnumUtils;

import java.util.Arrays;

/**
 * Created by yunyun on 2021/11/04.
 */
public enum CriteriaType {
    RESIDENTIAL_TYPE("residential-type"),
    PLACE_TYPE("place-type"),
    TAG("tag");

    private String criteriaTypeForUrl ;

    CriteriaType(String criteriaTypeForUrl){
        this.criteriaTypeForUrl = criteriaTypeForUrl;
    }

    public static CriteriaType findCriteriaTypeForUrl(String criteriaTypeForUrlRequest){
       return Arrays.stream(CriteriaType.values()).sequential()
               .filter( v -> v.getCriteriaTypeForUrl().equals(criteriaTypeForUrlRequest))
               .findAny().orElseThrow( () -> new CriteriaNotFountException(criteriaTypeForUrlRequest));
    }


    public String getCriteriaTypeForUrl(){
        return this.criteriaTypeForUrl;
    }
}
