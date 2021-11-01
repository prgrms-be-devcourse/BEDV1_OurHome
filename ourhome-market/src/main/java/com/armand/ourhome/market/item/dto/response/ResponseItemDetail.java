package com.armand.ourhome.market.item.dto.response;

import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.market.review.service.dto.ReviewDto;
import com.armand.ourhome.market.review.service.dto.response.PageResponse;
import com.armand.ourhome.market.review.service.dto.response.ResponseReview;
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

    @Builder
    public ResponseItemDetail(String name, String description, String imageUrl, int price, int stockQuantity, String companyName, Category category, LocalDateTime createdAt, PageResponse<List<ResponseReview>> reviews) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.companyName = companyName;
        this.category = category;
        this.createdAt = createdAt;
        this.serverDateTime = LocalDateTime.now();
        this.reviews = reviews;
    }
}
