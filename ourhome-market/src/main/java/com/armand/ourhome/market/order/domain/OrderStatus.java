package com.armand.ourhome.market.order.domain;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public enum OrderStatus {

    ACCEPTED(Constants.ACCEPTED), CANCELLED(Constants.CANCELLED);

    OrderStatus(String constant) {
    }

    public static class Constants {
        public static final String ACCEPTED = "ACCEPTED";
        public static final String CANCELLED = "CANCELLED";
    }
}
