package com.armand.ourhome.market.item.mapper;

import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.market.item.dto.ItemDto;
import com.armand.ourhome.market.item.dto.request.RequestSaveItem;
import com.armand.ourhome.market.item.dto.response.ResponseItem;
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

    default Item toItem(RequestSaveItem request, Company company) {

        return Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .category(request.getCategory())
                .price(request.getPrice())
                .company(company)
                .stockQuantity(request.getStockQuantity())
                .build();
    }

    ResponseItemDetail toResponseItemDetail(ItemDto itemDto, PageResponse<List<ResponseReview>> reviews);
}
