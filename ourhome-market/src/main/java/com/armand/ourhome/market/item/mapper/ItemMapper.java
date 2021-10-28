package com.armand.ourhome.market.item.mapper;

import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.market.item.dto.ItemDto;
import com.armand.ourhome.market.item.dto.request.RequestSaveItem;
import org.mapstruct.Mapper;

@Mapper
public interface ItemMapper {

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
}
