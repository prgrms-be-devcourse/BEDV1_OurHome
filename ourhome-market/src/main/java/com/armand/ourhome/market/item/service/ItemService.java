package com.armand.ourhome.market.item.service;

import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.market.item.dto.ItemDto;
import com.armand.ourhome.market.item.dto.response.ResponseItem;
import com.armand.ourhome.market.item.dto.response.ResponseItemDetail;
import com.armand.ourhome.market.item.exception.ItemNotFoundException;
import com.armand.ourhome.market.item.mapper.ItemMapper;
import com.armand.ourhome.market.review.service.ReviewService;
import com.armand.ourhome.market.review.service.dto.request.RequestReviewPages;
import com.armand.ourhome.market.review.service.dto.response.PageResponse;
import com.armand.ourhome.market.review.service.dto.response.ResponseReview;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ReviewService reviewService;
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    public ResponseItemDetail fetchItemDetailWithReview(Long itemId, RequestReviewPages request) {
        PageResponse<List<ResponseReview>> reviews = reviewService.fetchReviewPagesBy(itemId, request);
        ItemDto itemDto = findItemBy(itemId);
        return itemMapper.toResponseItemDetail(itemDto, reviews);
    }

    public List<ResponseItem> fetchItemPagesBy(Long lastItemId, int size) {
        Page<Item> items = fetchPages(lastItemId, size);

        return items.getContent().stream()
                .map(item -> itemMapper.toItemDto(item, item.getCompanyName()))
                .map(itemMapper::toResponseItem)
                .collect(Collectors.toList());
    }

    public ItemDto findItemBy(Long itemId) {
        Item item = getItem(itemId);
        return itemMapper.toItemDto(item, item.getCompanyName());
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
               .orElseThrow(() -> new ItemNotFoundException(MessageFormat.format("상품이 존재하지 않습니다. itemId = {0}", itemId)));
    }

    private Page<Item> fetchPages(Long lastItemId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);

        return itemRepository.findByIdLessThanOrderByIdDesc(lastItemId, pageRequest);
    }

}
