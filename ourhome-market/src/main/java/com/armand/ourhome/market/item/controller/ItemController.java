package com.armand.ourhome.market.item.controller;

import com.armand.ourhome.market.item.dto.ItemDto;
import com.armand.ourhome.market.item.dto.response.ResponseItemDetail;
import com.armand.ourhome.market.item.mapper.ItemMapper;
import com.armand.ourhome.market.item.service.ItemService;
import com.armand.ourhome.market.review.service.dto.request.RequestReviewPages;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/items")
@RestController
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @GetMapping("/{itemId}")
    public ResponseEntity<ResponseItemDetail> fetchItemDetail(@PathVariable("itemId") Long itemId, RequestReviewPages request) {
        ItemDto itemDto = itemService.findItemBy(itemId, request);

        return ResponseEntity.ok(itemMapper.toResponseItemDetail(itemDto));
    }
}
