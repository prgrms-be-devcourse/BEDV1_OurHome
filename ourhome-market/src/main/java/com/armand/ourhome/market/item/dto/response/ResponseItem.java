package com.armand.ourhome.market.item.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ResponseItem {

    private String name;
    private String companyName;
    private int price;

    @Builder
    public ResponseItem(String name, String companyName, int price) {
        this.name = name;
        this.companyName = companyName;
        this.price = price;
    }
}
