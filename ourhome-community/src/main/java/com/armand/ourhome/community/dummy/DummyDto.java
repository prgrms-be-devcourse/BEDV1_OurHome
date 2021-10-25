package com.armand.ourhome.community.dummy;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DummyDto {
    private Long dummyId;

    static public DummyDto of(Dummy dummy){
        return DummyDto.builder()
                .dummyId(dummy.getDummyId())
                .build();
    }
}
