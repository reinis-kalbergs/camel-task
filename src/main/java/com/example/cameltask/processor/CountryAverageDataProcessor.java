package com.example.cameltask.processor;

import com.example.cameltask.model.CountryAggregateData;
import com.example.cameltask.model.CountryData;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CountryAverageDataProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        CountryAggregateData body = exchange.getMessage().getBody(CountryAggregateData.class);

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
