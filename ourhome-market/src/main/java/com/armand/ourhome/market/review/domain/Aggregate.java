package com.armand.ourhome.market.review.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Aggregate {

    private final long count;
    private final double average;
}
