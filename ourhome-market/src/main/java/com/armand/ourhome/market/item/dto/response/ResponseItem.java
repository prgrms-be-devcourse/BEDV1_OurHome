package com.armand.ourhome.market.item.dto.response;

import com.armand.ourhome.market.item.dto.ItemDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ResponseItem {

    private List<ItemDto> items;
    @JsonProperty("server_date_time")
    private LocalDateTime serverDateTime;

    public ResponseItem(List<ItemDto> items) {
        this.items = items;
        this.serverDateTime = LocalDateTime.now();
    }
}
