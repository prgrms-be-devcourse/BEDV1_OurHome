package com.armand.ourhome.market.item.service;

import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.market.item.dto.ItemDto;
import com.armand.ourhome.market.item.dto.response.ResponseItem;
import com.armand.ourhome.market.review.domain.Aggregate;
import com.armand.ourhome.market.review.service.ReviewService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ReviewService reviewService;

    private Company company = new Company("com");

    private Item item = Item.builder()
            .category(Category.DAILY_NECESSITIES)
            .price(1000)
            .stockQuantity(100)
            .name("item")
            .company(company)
            .description("description")
            .imageUrl("image")
            .build();

    @Test
    @DisplayName("상품을 id로 단건 조회할 수 있다")
    public void testFindById() throws Exception {
        //given
        given(itemRepository.findById(any())).willReturn(Optional.of(item));

        //when
        ItemDto dto = itemService.findItemBy(1L);

        //then
        assertThat(dto.getName()).isEqualTo(item.getName());
        assertThat(dto.getPrice()).isEqualTo(item.getPrice());
        assertThat(dto.getCompanyName()).isEqualTo(item.getCompanyName());
        assertThat(dto.getStockQuantity()).isEqualTo(item.getStockQuantity());
    }
    
    @Test 
    @DisplayName("상품들을 리뷰수와 평점과 함께 조회한다")
    public void fetchItemPagesBy() throws Exception { 
        //given
        Aggregate aggregate = new Aggregate(10L, 3.25);
        List<Item> items = new ArrayList<>();
        items.add(item);

        Page<Item> page = new PageImpl<>(items);
        given(reviewService.getReviewAggregateBy(any())).willReturn(aggregate);
        given(itemRepository.findByIdLessThanOrderByIdDesc(any(), any())).willReturn(page);

        //when 
        ResponseItem responseItems = itemService.fetchItemPagesBy(1L, 1);

        //then
        assertThat(responseItems.getItems()).hasSize(1);
        assertThat(responseItems.getItems().get(0).getAverage()).isEqualTo(aggregate.getAverage());
        assertThat(responseItems.getItems().get(0).getCount()).isEqualTo(aggregate.getCount());
    } 
    
    
}