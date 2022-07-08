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
    private BigDecimal AverageUnitPrice;
    private BigDecimal averageUnitCost;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;
    private String csvFileName;
    private LocalDateTime ProcessingDate;
}
