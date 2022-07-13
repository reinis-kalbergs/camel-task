package com.example.cameltask.processor;

import com.example.cameltask.model.RegionAggregateData;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class RegionDataToListProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        RegionAggregateData regionAggregateData = exchange.getMessage().getBody(RegionAggregateData.class);
        exchange.getMessage().setBody(regionAggregateData.getRegionData());
    }
}
