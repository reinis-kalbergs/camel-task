package com.example.cameltask.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CsvRecord(separator = ",", skipFirstLine = true, generateHeaderColumns = true)
public class CountryData {

    @DataField(pos = 1)
    private String country;
    private String region;
    @DataField(pos = 2)
    private Long orderCount;
    @DataField(pos = 3, precision = 2, pattern = "00.00")
    private BigDecimal averageUnitsSold;
    @DataField(pos = 4, precision = 2, pattern = "00.00")
    private BigDecimal averageUnitPrice;
    @DataField(pos = 5, precision = 2, pattern = "00.00")
    private BigDecimal averageUnitCost;
    @DataField(pos = 6, precision = 2, pattern = "00.00")
    private BigDecimal totalRevenue;
    @DataField(pos = 7, precision = 2, pattern = "00.00")
    private BigDecimal totalCost;
    @DataField(pos = 8, precision = 2, pattern = "00.00")
    private BigDecimal totalProfit;
}
