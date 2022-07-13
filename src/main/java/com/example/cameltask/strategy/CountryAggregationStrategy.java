package com.example.cameltask.strategy;

import com.example.cameltask.model.CountryAggregateData;
import com.example.cameltask.model.IncomingOrder;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class CountryAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            IncomingOrder body = newExchange.getIn().getBody(IncomingOrder.class);

            CountryAggregateData countryTotalsData =
                    CountryAggregateData.builder()
                            .country(body.getCountry())
                            .region(body.getRegion())
                            .orderCount(1L)
                            .unitsSold(body.getUnitsSold())
                            .unitPrice(body.getUnitPrice())
                            .unitCost(body.getUnitCost())
                            .totalRevenue(body.getTotalRevenue())
                            .totalCost(body.getTotalCost())
                            .totalProfit(body.getTotalProfit())
                            .build();

            newExchange.getMessage().setBody(countryTotalsData);
            return newExchange;
        }
        CountryAggregateData oldBody = oldExchange.getIn().getBody(CountryAggregateData.class);
        IncomingOrder newBody = newExchange.getIn().getBody(IncomingOrder.class);

        CountryAggregateData aggregatedData = CountryAggregateData.builder()
                .country(oldBody.getCountry())
                .region(oldBody.getRegion())
                .orderCount(oldBody.getOrderCount() + 1)
                .unitsSold(oldBody.getUnitsSold() + newBody.getUnitsSold())
                .unitPrice(oldBody.getUnitPrice().add(newBody.getUnitPrice()))
                .unitCost(oldBody.getUnitCost().add(newBody.getUnitCost()))
                .totalRevenue(oldBody.getTotalRevenue().add(newBody.getTotalRevenue()))
                .totalCost(oldBody.getTotalCost().add(newBody.getTotalCost()))
                .totalProfit(oldBody.getTotalProfit().add(newBody.getTotalProfit()))
                .build();

        oldExchange.getIn().setBody(aggregatedData);
        return oldExchange;
    }
}
