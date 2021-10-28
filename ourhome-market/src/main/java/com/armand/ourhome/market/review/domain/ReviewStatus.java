package com.armand.ourhome.market.review.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReviewStatus {

    POSTED(true),
    DELETED(false);

    private final boolean visible;
}
