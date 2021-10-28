package com.armand.ourhome.market.item.dto;


import com.armand.ourhome.domain.item.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ItemDto {

    private String name;
    private String description;
    private String imageUrl;
    private int price;
    private int stockQuantity;
    private String companyName;
    private Category category;
    private LocalDateTime createdAt;

    @Builder
    public ItemDto(String name, String description, String imageUrl, int price, int stockQuantity, String companyName, Category category, LocalDateTime createdAt) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.companyName = companyName;
        this.category = category;
        this.createdAt = createdAt;
    }
}
