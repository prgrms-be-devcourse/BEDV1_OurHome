package com.armand.ourhome.market.item.dto;


import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.market.review.dto.ReviewDto;
import com.armand.ourhome.market.review.dto.response.PageResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ItemDto {

    @JsonProperty("item_id")
    private Long itemId;
    private String name;
    @JsonProperty("image_url")
    private String imageUrl;
    private int price;
    @JsonProperty("stock_quantity")
    private int stockQuantity;
    @JsonProperty("company_name")
    private String companyName;
    private Category category;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    private long count;
    private double average;

    @Builder
    public ItemDto(Long itemId, String name, String imageUrl, int price, int stockQuantity, String companyName, Category category, LocalDateTime createdAt, long count, double average) {
        this.itemId = itemId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.companyName = companyName;
        this.category = category;
        this.createdAt = createdAt;
        this.count = count;
        this.average = average;
    }
}
