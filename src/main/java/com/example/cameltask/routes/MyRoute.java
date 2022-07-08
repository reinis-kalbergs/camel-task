package com.example.cameltask.routes;

import com.example.cameltask.filter.OnlineSalesChannelFilter;
import com.example.cameltask.model.IncomingOrder;
import com.example.cameltask.processor.CountryDataProcessor;
import com.example.cameltask.processor.OrderToDatabaseProcessor;
import com.example.cameltask.strategy.CountryAggregationStrategy;
import com.example.cameltask.strategy.RegionAggregationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MyRoute extends RouteBuilder {
    //@formatter:off
    private final OrderToDatabaseProcessor orderToDatabaseProcessor;
    private final String COUNTRY_HEADER = "country";
    private final String REGION_HEADER = "region";

    @Override
    public void configure() throws Exception {

        from("file:in?noop=true")
                .unmarshal()
                    .bindy(BindyType.Csv, IncomingOrder.class)
                .split(body())
                .filter()
                    .method(OnlineSalesChannelFilter.class, "isOnline")
                .process(orderToDatabaseProcessor)
                .process(exchange -> {
                    Message message = exchange.getMessage();
                    IncomingOrder incomingOrder = message.getBody(IncomingOrder.class);
                    message.setHeader(COUNTRY_HEADER, incomingOrder.getCountry());
                    message.setHeader(REGION_HEADER, incomingOrder.getRegion());
                })
                .aggregate(header(COUNTRY_HEADER), new CountryAggregationStrategy())
                    .completionTimeout(1000)
                .process(new CountryDataProcessor())
                .aggregate(header(REGION_HEADER), new RegionAggregationStrategy())
                    .completionTimeout(1000)
                .log("${body}");


//        from("timer:first-timer")
//                .bean("currentTime")
//                .process(new MyLoggingProcessor())
//                .to("log:first-timer");

        //getIn() vs getMessage()
        //make region and country headers some kind of const
    }
}
