package com.example.cameltask.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryTotalsData {

    private String country;
    private String region;
    private Long orderCount;
    private Long unitsSold;
    private BigDecimal unitPrice;
    private BigDecimal unitCost;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;

    public BigDecimal getCountAsBigDecimal() {
        return new BigDecimal(orderCount);
    }
}
