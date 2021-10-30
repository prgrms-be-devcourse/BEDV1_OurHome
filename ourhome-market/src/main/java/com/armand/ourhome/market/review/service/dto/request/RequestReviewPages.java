package com.armand.ourhome.market.review.service.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;

@Getter
@NoArgsConstructor
public class RequestReviewPages {

    private static final int DEFAULT_SIZE = 5;

    private int page;
    private OrderType order;
    private int size;

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setOrder(OrderType order) {
        this.order = order == null ? OrderType.BEST : order;
    }

    public void setSize(int size) {
        this.size = size <= 0 ? DEFAULT_SIZE : size;
    }

    public PageRequest of() {

        if (order == OrderType.RECENT)
            return PageRequest.of(page - 1, size, Sort.Direction.DESC, order.getOrder());
        return PageRequest.of(page - 1, size, Sort.Direction.DESC, order.getOrder(), OrderType.RECENT.getOrder());
    }

    @Getter
    @RequiredArgsConstructor
    enum OrderType{
        BEST("help"), RECENT("id");

        private final String order;
    }
}
