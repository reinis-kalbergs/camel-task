package com.example.cameltask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@CsvRecord(separator = ",", skipFirstLine = true)
public class IncomingOrder implements Serializable {

    @DataField(pos = 1)
    private String region;
    @DataField(pos = 2)
    private String country;
    @DataField(pos = 3)
    private String itemType;
    @DataField(pos = 4)
    private String salesChannel;
    @DataField(pos = 5)
    private String orderPriority;
    @DataField(pos = 6, pattern = "M/d/yyyy")
    private LocalDate orderDate;
    @DataField(pos = 7)
    private Long orderId;
    @DataField(pos = 8, pattern = "M/d/yyyy")
    private LocalDate shipDate;
    @DataField(pos = 9)
    private Long unitsSold;
    @DataField(pos = 10, precision = 2)
    private BigDecimal unitPrice;
    @DataField(pos = 11, precision = 2)
    private BigDecimal unitCost;
    @DataField(pos = 12, precision = 2)
    private BigDecimal totalRevenue;
    @DataField(pos = 13, precision = 2)
    private BigDecimal totalCost;
    @DataField(pos = 14, precision = 2)
    private BigDecimal totalProfit;

}
