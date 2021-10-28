package com.armand.ourhome.market.item.dto.request;

import com.armand.ourhome.domain.item.domain.Category;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class RequestSaveItem {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String imageUrl;
    @NotNull
    private int price;
    @NotNull
    private int stockQuantity;
    @NotNull
    private Long companyId;
    @NotNull
    private Category category;

    @Builder
    public RequestSaveItem(String name, String description, String imageUrl, int price, int stockQuantity, Long companyId, Category category) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.companyId = companyId;
        this.category = category;
    }
}
