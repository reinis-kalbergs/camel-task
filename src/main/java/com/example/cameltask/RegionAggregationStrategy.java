package com.example.cameltask;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class RegionAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            return newExchange;
        }


        return null;
    }


}
