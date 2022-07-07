package com.example.cameltask.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Orders")
@Data
@NoArgsConstructor
public class Order {
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

    public Order(IncomingOrder incomingOrder){
        this.orderId=incomingOrder.getOrderId();
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
