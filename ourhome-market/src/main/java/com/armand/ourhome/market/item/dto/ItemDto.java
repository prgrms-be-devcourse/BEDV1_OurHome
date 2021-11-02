package com.armand.ourhome.market.item.dto;


import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.market.review.dto.ReviewDto;
import com.armand.ourhome.market.review.dto.response.PageResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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
