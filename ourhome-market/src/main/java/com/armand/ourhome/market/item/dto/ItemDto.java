package com.armand.ourhome.market.item.dto;


import com.armand.ourhome.domain.item.domain.Category;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long itemId;
    private String name;
    private String imageUrl;
    private int price;
    private int stockQuantity;
    private String companyName;
    private Category category;
    private LocalDateTime createdAt;
    private long count;
    private double average;
}
