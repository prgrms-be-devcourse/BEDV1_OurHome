package com.armand.ourhome.market.review.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
public class RequestReviewPages {

    private static final int DEFAULT_SIZE = 5;

    private int page;
    private OrderType order;
    private int size;

    public PageRequest of() {
        setDefaultValueIfInvalid();
        if (order == OrderType.RECENT)
            return PageRequest.of(page - 1, size, Sort.Direction.DESC, order.getOrder());
        return PageRequest.of(page - 1, size, Sort.Direction.DESC, order.getOrder(), OrderType.RECENT.getOrder());
    }

    private void setDefaultValueIfInvalid() {
        page = page <= 0 ? 1 : page;
        order = order == null ? OrderType.BEST : order;
        size = size <= 0 ? DEFAULT_SIZE : size;
    }

    @Getter
    @RequiredArgsConstructor
    enum OrderType{
        BEST("help"), RECENT("id");

        private final String order;
    }
}
