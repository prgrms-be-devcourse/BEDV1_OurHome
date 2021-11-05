package com.armand.ourhome.market.item.mapper;

import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.market.item.dto.ItemDto;
import com.armand.ourhome.market.item.dto.response.ResponseItemDetail;
import com.armand.ourhome.market.review.domain.Aggregate;
import com.armand.ourhome.market.review.dto.response.PageResponse;
import com.armand.ourhome.market.review.dto.response.ResponseReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ItemMapper {

    @Mapping(target = "count", source = "aggregate.count")
    @Mapping(target = "average", source = "aggregate.average")
    @Mapping(target = "itemId", source = "item.id")
    ItemDto toItemDto(Item item, Aggregate aggregate);

    @Mapping(target = "count", source = "aggregate.count")
    @Mapping(target = "average", source = "aggregate.average")
    ResponseItemDetail toResponseItemDetail(Item item, PageResponse<List<ResponseReview>> reviews, Aggregate aggregate);
}
