package com.armand.ourhome.market.item.dto.response;

import com.armand.ourhome.common.api.PageResponse;
import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.market.review.dto.response.ResponseReview;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseItemDetail {

    private String name;
    private String description;
    private String imageUrl;
    private int price;
    private int stockQuantity;
    private String companyName;
    private Category category;
    private LocalDateTime createdAt;
    private PageResponse<List<ResponseReview>> reviews;
    private LocalDateTime serverDateTime;
    private long count;
    private double average;

    @Builder
    public ResponseItemDetail(String name, String description, String imageUrl, int price, int stockQuantity,
                              String companyName, Category category, LocalDateTime createdAt,
                              PageResponse<List<ResponseReview>> reviews, long count, double average) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.companyName = companyName;
        this.category = category;
        this.createdAt = createdAt;
        this.reviews = reviews;
        this.serverDateTime = LocalDateTime.now();
        this.count = count;
        this.average = average;
    }
}
