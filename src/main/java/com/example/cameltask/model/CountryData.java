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
public class CountryData {

    private String country;
    private String region;
    private Long orderCount;
    private BigDecimal averageUnitsSold;
    private BigDecimal averageUnitPrice;
    private BigDecimal averageUnitCost;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;
}
