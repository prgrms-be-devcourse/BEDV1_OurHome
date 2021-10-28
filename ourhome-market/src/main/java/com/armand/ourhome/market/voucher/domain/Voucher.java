package com.armand.ourhome.market.voucher.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn(name = "DTYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Entity
@Table(name = "voucher"
//    , uniqueConstraints = {
//    @UniqueConstraint(columnNames = {"amount", "percent", "minLimit"})}
)
public abstract class Voucher {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "min_limit", nullable = false)
  private Integer minLimit;

  public Voucher(Integer minLimit) {
    this.minLimit = minLimit;
  }

  public int discount(int currentPrice) {
    return currentPrice - getDiscountPrice(currentPrice);
  }

  public abstract int getDiscountPrice(int currentPrice);

}
