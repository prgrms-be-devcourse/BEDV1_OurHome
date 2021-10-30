package com.armand.ourhome.market.item.service;

import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.repository.CompanyRepository;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.market.item.dto.ItemDto;
import com.armand.ourhome.market.item.dto.request.RequestSaveItem;
import com.armand.ourhome.market.item.dto.response.ResponseItemDetail;
import com.armand.ourhome.market.review.service.ReviewService;
import com.armand.ourhome.market.review.service.dto.request.RequestReviewPages;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ReviewService reviewService;

    private Item item;
    private Company company;

    @BeforeAll
    void setup() {
        company = new Company("com");
        item = Item.builder()
                .category(Category.DAILY_NECESSITIES)
                .price(1000)
                .stockQuantity(100)
                .name("item")
                .company(company)
                .description("description")
                .imageUrl("image")
                .build();
    }

    @Test
    @DisplayName("상품을 id로 단건 조회할 수 있다")
    public void testFindById() throws Exception {
        //given
        given(itemRepository.findById(any())).willReturn(Optional.of(item));

        given(reviewService.fetchReviewPagesBy(any(), any())).willReturn(null);

        //when
        ItemDto dto = itemService.findItemBy(1L, new RequestReviewPages());

        //then
        assertThat(dto.getName()).isEqualTo(item.getName());
        assertThat(dto.getPrice()).isEqualTo(item.getPrice());
        assertThat(dto.getCompanyName()).isEqualTo(item.getCompanyName());
        assertThat(dto.getStockQuantity()).isEqualTo(item.getStockQuantity());
    }
}