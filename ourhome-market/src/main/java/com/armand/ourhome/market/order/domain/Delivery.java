package com.armand.ourhome.market.order.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "delivery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    public static final DeliveryStatus DEFAULT_DELIVERY_STATUS = DeliveryStatus.READY_FOR_DELIVERY;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeliveryStatus status;

    @Column(name = "code", nullable = false, unique = true)
    private UUID code;

    @Builder
    public Delivery(DeliveryStatus status, UUID code) {
        this.status = status;
        this.code = code;
    }

    static public Delivery newInstance() {
        return Delivery.builder().build();
    }

    @PrePersist
    public void prePersist() {
        this.status = this.status == null ? DEFAULT_DELIVERY_STATUS : this.status;
        this.code = this.code == null ? UUID.randomUUID() : this.code;
    }

    public void updateStatus(DeliveryStatus deliveryStatus) {
        this.status = deliveryStatus;
    }
}
