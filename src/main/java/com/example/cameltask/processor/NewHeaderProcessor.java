package com.example.cameltask.processor;

import com.example.cameltask.model.IncomingOrder;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewHeaderProcessor implements Processor {
    @Value("${camel-task.headers.country}")
    private String COUNTRY_HEADER;
    @Value("${camel-task.headers.region}")
    private String REGION_HEADER;

    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getIn();
        IncomingOrder incomingOrder = exchange.getIn().getBody(IncomingOrder.class);
        String region = incomingOrder.getRegion();
        String country = incomingOrder.getCountry();
        exchange.getMessage().setHeader(COUNTRY_HEADER, region);
        exchange.getMessage().setHeader(REGION_HEADER, country);
    }
}
