package com.example.cameltask.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class OrderEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long orderId;
    private LocalDate orderDate;
    private String orderPriority;
    private LocalDate shippingDate;
    private BigDecimal cost;
    private BigDecimal revenue;
    private BigDecimal profit;
    private String itemType;
    private String country;

    public OrderEntity(IncomingOrder incomingOrder) {
        this.orderId = incomingOrder.getOrderId();
        this.orderDate = incomingOrder.getOrderDate();
        this.orderPriority = incomingOrder.getOrderPriority();
        this.shippingDate = incomingOrder.getShipDate();
        this.cost = incomingOrder.getTotalCost();
        this.revenue = incomingOrder.getTotalRevenue();
        this.profit = incomingOrder.getTotalProfit();
        this.itemType = incomingOrder.getItemType();
        this.country = incomingOrder.getCountry();
    }
}
