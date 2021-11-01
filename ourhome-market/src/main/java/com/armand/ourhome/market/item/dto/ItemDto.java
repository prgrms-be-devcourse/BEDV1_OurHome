package com.armand.ourhome.market.item.dto;


import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.market.review.service.dto.ReviewDto;
import com.armand.ourhome.market.review.service.dto.response.PageResponse;
import com.armand.ourhome.market.review.service.dto.response.ResponseReview;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ItemDto {

    private Long itemId;
    private String name;
    private String description;
    private String imageUrl;
    private int price;
    private int stockQuantity;
    private String companyName;
    private Category category;
    private LocalDateTime createdAt;
    private PageResponse<List<ReviewDto>> reviews;

    @Builder
    public ItemDto(Long itemId, String name, String description, String imageUrl, int price, int stockQuantity, String companyName, Category category, LocalDateTime createdAt) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.companyName = companyName;
        this.category = category;
        this.createdAt = createdAt;
        this.reviews = reviews;
    }
}
