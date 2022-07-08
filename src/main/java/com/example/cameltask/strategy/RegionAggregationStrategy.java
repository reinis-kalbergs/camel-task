package com.example.cameltask.strategy;

import com.example.cameltask.model.CountryData;
import com.example.cameltask.model.RegionData;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class RegionAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            CountryData data = newExchange.getIn().getBody(CountryData.class);

            RegionData regionData = new RegionData();
            regionData.addCountryData(data);

            newExchange.getMessage().setBody(regionData);
            return newExchange;
        }

        RegionData oldBody = oldExchange.getMessage().getBody(RegionData.class);
        CountryData newBody = newExchange.getMessage().getBody(CountryData.class);

        oldBody.addCountryData(newBody);
        oldExchange.getMessage().setBody(oldBody);
        return oldExchange;
    }
}
