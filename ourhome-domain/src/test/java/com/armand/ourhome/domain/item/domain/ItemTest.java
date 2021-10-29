package com.armand.ourhome.domain.item.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemTest {

    @Test
    @DisplayName("상품이 생성된다.")
    public void testCreateItem() throws Exception {
        Item item = getItem(1000, 100);

        assertThat(item.getName()).isEqualTo("item");
    }
    
    @Test
    @DisplayName("재고 수량은 0 이상 이어야 한다.")
    public void testStockQuantityUnder0() throws Exception {
        //given
        int stockQuantity = -1;
        //then
        assertThrows(IllegalArgumentException.class, () -> {
            //when
            Item item = getItem(stockQuantity, 100);
        });
    }

    private Item getItem(int stockQuantity, int price) {
        return Item.builder()
                .stockQuantity(stockQuantity)
                .imageUrl("imageUrl")
                .description("description")
                .category(Category.DAILY_NECESSITIES)
                .company(new Company("company"))
                .name("item")
                .price(price)
                .build();
    }


    @Test
    @DisplayName("상품 가격은 1이상 이어야 한다.")
    public void testPriceUnder1() throws Exception {
        //given
        int price = 0;
        int stockQuantity = 1000;
        //then
        assertThrows(IllegalArgumentException.class, () -> {
            //when
            Item item = getItem(stockQuantity, price);
        });
    }

    @Test
    @DisplayName("제거할 상품 재고 수량은 음수가 될 수 없다.")
    public void testRemoveStockQuantityNegative() throws Exception {
        //given
        int price = 100;
        int stockQuantity = 1000;
        Item item = getItem(stockQuantity, price);
        //then
        assertThrows(IllegalArgumentException.class, () -> {
            //when
            item.removeStockQuantity(-1);
        });
    }

    @Test
    @DisplayName("제거할 재고 수량이 현 재고 수량보다 클 수 없다.")
    public void testRemoveStockQuantityOver() throws Exception {
        //given
        int price = 100;
        int stockQuantity = 1000;
        Item item = getItem(stockQuantity, price);
        //then
        assertThrows(IllegalArgumentException.class, () -> {
            //when
            item.removeStockQuantity(stockQuantity + 1);
        });
    }

    @Test
    @DisplayName("추가 재고 수량은 음수가 될 수 없다.")
    public void testAddStockQuantityOver() throws Exception {
        //given
        int price = 100;
        int stockQuantity = 1000;
        Item item = getItem(stockQuantity, price);
        //then
        assertThrows(IllegalArgumentException.class, () -> {
            //when
            item.addStockQuantity(-1);
        });
    }
}