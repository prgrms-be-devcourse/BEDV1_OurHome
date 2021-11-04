package com.armand.ourhome.market.item.controller;

import com.armand.ourhome.market.item.dto.response.ResponseItem;
import com.armand.ourhome.market.item.dto.response.ResponseItemDetail;
import com.armand.ourhome.market.item.mapper.ItemMapper;
import com.armand.ourhome.market.item.service.ItemService;
import com.armand.ourhome.market.review.dto.request.RequestReviewPages;
import com.armand.ourhome.market.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/items")
@RestController
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ResponseEntity<ResponseItemDetail> fetchItemDetail(@PathVariable("itemId") Long itemId, RequestReviewPages request) {

        return ResponseEntity.ok(itemService.fetchItemDetailWithReview(itemId, request));
    }

    @GetMapping
    public ResponseEntity<ResponseItem> fetchItemPages(@RequestParam Long lastItemId, @RequestParam int size) {
        return ResponseEntity.ok(itemService.fetchItemPagesBy(lastItemId, size));
    }
}
