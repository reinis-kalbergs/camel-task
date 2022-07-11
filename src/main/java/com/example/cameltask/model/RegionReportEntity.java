package com.example.cameltask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "region_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionReportEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String region;
    private String country;
    private Long orderCount;
    private BigDecimal averageUnitsSold;
    private BigDecimal averageUnitPrice;
    private BigDecimal averageUnitCost;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;
    private String csvFileName;
    private LocalDateTime processingDate;

    public RegionReportEntity(CountryData countryData) {
        this.country = countryData.getCountry();
        this.orderCount = countryData.getOrderCount();
        this.averageUnitsSold = countryData.getAverageUnitsSold();
        this.averageUnitPrice = countryData.getAverageUnitPrice();
        this.averageUnitCost = countryData.getAverageUnitCost();
        this.totalRevenue = countryData.getTotalRevenue();
        this.totalCost = countryData.getTotalCost();
        this.totalProfit = countryData.getTotalProfit();
    }
}
