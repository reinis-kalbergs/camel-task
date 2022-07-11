package com.example.cameltask.processor;

import com.example.cameltask.model.CountryData;
import com.example.cameltask.model.CountryTotalsData;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CountryDataProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        CountryTotalsData body = exchange.getMessage().getBody(CountryTotalsData.class);

        CountryData countryData = CountryData.builder()
                .country(body.getCountry())
                .orderCount(body.getOrderCount())
                .averageUnitsSold(BigDecimal.valueOf(body.getUnitsSold() / body.getOrderCount()))
                .averageUnitPrice(body.getUnitPrice().divide(body.getCountAsBigDecimal(), RoundingMode.HALF_UP))
                .averageUnitCost(body.getUnitCost().divide(body.getCountAsBigDecimal(), RoundingMode.HALF_UP))
                .totalRevenue(body.getTotalRevenue().divide(BigDecimal.valueOf(1000000L), RoundingMode.HALF_UP))
                .totalCost(body.getTotalCost().divide(BigDecimal.valueOf(1000000L), RoundingMode.HALF_UP))
                .totalProfit(body.getTotalProfit().divide(BigDecimal.valueOf(1000000L), RoundingMode.HALF_UP))
                .build();

        exchange.getMessage().setBody(countryData);
    }
}
