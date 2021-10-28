package com.armand.ourhome.market.item.mapper;

import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.market.item.dto.ItemDto;
import com.armand.ourhome.market.item.dto.ItemDto.ItemDtoBuilder;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-10-28T14:25:27+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.1 (Oracle Corporation)"
)
public class ItemMapperImpl implements ItemMapper {

    @Override
    public ItemDto toItemDto(Item item, String companyName) {
        if ( item == null && companyName == null ) {
            return null;
        }

        ItemDtoBuilder itemDto = ItemDto.builder();

        if ( item != null ) {
            itemDto.name( item.getName() );
            itemDto.description( item.getDescription() );
            itemDto.imageUrl( item.getImageUrl() );
            itemDto.price( item.getPrice() );
            itemDto.stockQuantity( item.getStockQuantity() );
            itemDto.companyName( item.getCompanyName() );
            itemDto.category( item.getCategory() );
            itemDto.createdAt( item.getCreatedAt() );
        }

        return itemDto.build();
    }
}
