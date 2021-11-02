package com.armand.ourhome.market.item.dto.response;

import com.armand.ourhome.market.item.dto.ItemDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseItem {

    private List<ItemDto> items;
    private LocalDateTime serverDateTime;

    @Builder
    public ResponseItem(List<ItemDto> items) {
        this.items = items;
        this.serverDateTime = LocalDateTime.now();
    }
}
