package com.armand.ourhome.domain.item.domain;

import com.armand.ourhome.domain.base.BaseEntity;
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
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    private ItemDetail itemDetail;

    @Column(nullable = false)
    private int price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Builder
    public Item(String name, String description, String imageUrl, int price, int stockQuantity, Company company, Category category) {

        validate(price, stockQuantity, company, category);

        this.itemDetail = ItemDetail.of(name, description, imageUrl);
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.company = company;
        this.category = category;
    }

    //== Business Method ==//
    public void update(String name, String description, String imageUrl, int price, int stockQuantity, Category category) {

        validate(price, stockQuantity, this.company, category);

        this.itemDetail = ItemDetail.of(name, description, imageUrl);
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
    }

    public void updateInfo(String name, String description, String imageUrl) {
        this.itemDetail = ItemDetail.of(name, description, imageUrl);
    }

    public void updatePrice(int price) {
        validatePrice(price);

        this.price = price;
    }

    public void updateStockQuantity(int stockQuantity) {
        validateQuantityOverZero(stockQuantity);

        this.stockQuantity = stockQuantity;
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
    private void validate(int price, int quantity, Company company,
        Category category) {
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

    public String getName() {
        return this.itemDetail.getName();
    }

    public String getDescription() {
        return this.itemDetail.getDescription();
    }

    public String getImageUrl() {
        return this.itemDetail.getImageUrl();
    }

    public String getCompanyName() {
        return this.company.getName();
    }
}
