package com.armand.ourhome.domain.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.text.MessageFormat;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    private ItemDetail itemDetail;

    @Column(nullable = false)
    private int price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Builder
    public Item(ItemDetail itemDetail, int price, int stock, Company company, Category category) {

        validate(itemDetail, price, stock, company, category);

        this.itemDetail = itemDetail;
        this.price = price;
        this.stockQuantity = stock;
        this.company = company;
        this.category = category;
    }

    //== Business Method ==//
    public Item update(ItemDetail itemDetail, int price, int stockQuantity, Category category) {

        validate(itemDetail, price, stockQuantity, this.company, category);

        this.itemDetail = itemDetail;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;

        return this;
    }

    public int addStockQuantity(int quantity) {

        validateQuantityOverZero(quantity);

        this.stockQuantity += quantity;

        return this.stockQuantity;
    }

    public int removeStockQuantity(int quantity) {

        validateQuantityOverZero(quantity);

        if (this.stockQuantity < quantity) {
            throw new IllegalArgumentException(
                MessageFormat.format("재고가 부족합니다. stock : {0}, count : {1}", this.stockQuantity,
                    quantity));
        }

        this.stockQuantity -= quantity;

        return this.stockQuantity;
    }

    //== Validation Method ==//
    private void validate(ItemDetail itemDetail, int price, int quantity, Company company,
        Category category) {
        Assert.notNull(itemDetail, "상품 상세 정보는 null이 될 수 없습니다.");
        Assert.notNull(company, "기업은 null이 될 수 없습니다.");
        Assert.notNull(category, "상품 카테고리는 null이 될 수 없습니다.");

        validatePrice(price);
        validateQuantityOverZero(quantity);
    }

    private void validatePrice(int price) {
        if (price <= 0) {
            throw new IllegalArgumentException(
                MessageFormat.format("상품 가격은 0 이하가 될 수 없습니다. price = {0}", price));
        }
    }

    private void validateQuantityOverZero(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(
                MessageFormat.format("재고 수량은 0 이상이어야 합니다. quantity: {0}", quantity));
        }
    }

}
