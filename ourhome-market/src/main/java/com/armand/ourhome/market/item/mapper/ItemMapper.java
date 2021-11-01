package com.armand.ourhome.market.item.mapper;

import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.market.item.dto.ItemDto;
import com.armand.ourhome.market.item.dto.request.RequestSaveItem;
import com.armand.ourhome.market.item.dto.response.ResponseItem;
import com.armand.ourhome.market.item.dto.response.ResponseItemDetail;
import com.armand.ourhome.market.review.service.dto.ReviewDto;
import com.armand.ourhome.market.review.service.dto.response.PageResponse;
import com.armand.ourhome.market.review.service.dto.response.ResponseReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ItemMapper {

    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "reviews", source = "reviews")
    ItemDto toItemDto(Item item, String companyName, PageResponse<List<ReviewDto>> reviews);

    @Mapping(target = "reviews", ignore = true)
    ItemDto toItemDto(Item item, String companyName);

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

    ResponseItem toResponseItem(ItemDto dto);
}
