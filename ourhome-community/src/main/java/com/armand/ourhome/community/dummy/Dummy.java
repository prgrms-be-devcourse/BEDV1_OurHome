package com.armand.ourhome.community.dummy;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "dummy")
public class Dummy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dummy_id")
    private Long dummyId;

    @Builder
    private Dummy(Long dummyId) {
        this.dummyId = dummyId;
    }

    static public Dummy of(DummyDto dummyDto){
        return Dummy.builder()
                .dummyId(dummyDto.getDummyId())
                .build();
    }
}
