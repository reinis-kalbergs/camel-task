package com.example.cameltask.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RegionReport {

    private Long id;
    private String region;
    private String country;
    private Long orderCount;
    private BigDecimal averageUnitsSold;
    private BigDecimal AverageUnitPrice;
    private BigDecimal averageUnitCost;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;
    private String csvFileName;
    private LocalDate ProcessingDate;
}
