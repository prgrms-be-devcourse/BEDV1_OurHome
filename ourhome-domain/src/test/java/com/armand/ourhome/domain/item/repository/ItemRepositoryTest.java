package com.armand.ourhome.domain.item.repository;

import com.armand.ourhome.domain.OurHomeDomainConfig;
import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private List<Item> items = new ArrayList<>();
    private Company company;
    @BeforeAll
    void setup() {
        company = new Company("company");
        for (int i = 0; i < 100; i++) {
            Item item = Item.builder()
                    .category(Category.DAILY_NECESSITIES)
                    .price(1000)
                    .stockQuantity(100)
                    .name("item" + i)
                    .company(company)
                    .description("description")
                    .imageUrl("image")
                    .build();
            items.add(item);
        }

    }


    @Test
    public void testSetup() throws Exception {
        companyRepository.save(company);
        itemRepository.saveAll(items);

        assertThat(itemRepository.count()).isEqualTo(100);
    }

    @Test
    @DisplayName("상품을 페이징 조회할 수 있다.")
    public void testFetchItemPages() throws Exception {
        //given
        companyRepository.save(company);
        itemRepository.saveAll(items);
        
        Long lastItemId = 20L;
        int size = 10;
        
        //when
        Page<Item> pages = itemRepository.findByIdLessThanOrderByIdDesc(lastItemId, PageRequest.of(0, size));

        //then
        List<Item> content = pages.getContent();
        assertThat(content).hasSize(size);
        assertThat(content.get(0).getId()).isEqualTo(lastItemId - 1);
        assertThat(content.get(size - 1).getId()).isEqualTo(lastItemId - size);
    }
}