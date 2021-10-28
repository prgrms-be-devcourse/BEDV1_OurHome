package com.armand.ourhome.market.item.service;

import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.repository.CompanyRepository;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.market.item.dto.ItemDto;
import com.armand.ourhome.market.item.dto.request.RequestSaveItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private CompanyRepository companyRepository;

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
    @DisplayName("상품이 저장된다")
    public void testSaveItem() throws Exception {
        //given
        RequestSaveItem request = RequestSaveItem.builder()
                .category(item.getCategory())
                .companyId(1L)
                .description(item.getDescription())
                .imageUrl(item.getImageUrl())
                .price(item.getPrice())
                .name(item.getName())
                .stockQuantity(item.getStockQuantity())
                .build();

        given(itemRepository.save(any())).willReturn(item);
        given(companyRepository.findById(any())).willReturn(Optional.of(company));

        //when
        ItemDto saveDto = itemService.save(request);

        //then
        assertThat(saveDto).isNotNull();
        assertThat(saveDto.getName()).isEqualTo(item.getName());
        assertThat(saveDto.getDescription()).isEqualTo(item.getDescription());
        assertThat(saveDto.getPrice()).isEqualTo(item.getPrice());
        assertThat(saveDto.getStockQuantity()).isEqualTo(item.getStockQuantity());
    }


}