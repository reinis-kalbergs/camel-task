package com.example.cameltask.strategy;

import com.example.cameltask.model.CountryData;
import com.example.cameltask.model.RegionAggregateData;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class RegionAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            CountryData data = newExchange.getIn().getBody(CountryData.class);

            RegionAggregateData regionAggregateData = new RegionAggregateData();
            regionAggregateData.addCountryData(data);

            newExchange.getMessage().setBody(regionAggregateData);
            return newExchange;
        }

        RegionAggregateData oldBody = oldExchange.getMessage().getBody(RegionAggregateData.class);
        CountryData newBody = newExchange.getMessage().getBody(CountryData.class);

        oldBody.addCountryData(newBody);
        oldExchange.getMessage().setBody(oldBody);
        return oldExchange;
    }
}
