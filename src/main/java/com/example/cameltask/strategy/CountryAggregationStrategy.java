package com.example.cameltask.strategy;

import com.example.cameltask.model.IncomingOrder;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class CountryAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            return newExchange;
        }
        IncomingOrder oldBody = oldExchange.getIn().getBody(IncomingOrder.class);
        IncomingOrder newBody = oldExchange.getIn().getBody(IncomingOrder.class);
        String aggregated = "c+" + oldBody.getCountry() + "c+" + newBody.getCountry();
        oldExchange.getIn().setBody(aggregated);
        return oldExchange;
    }
}
