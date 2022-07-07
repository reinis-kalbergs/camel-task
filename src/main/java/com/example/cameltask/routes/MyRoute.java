package com.example.cameltask.routes;

import com.example.cameltask.model.IncomingOrder;
import com.example.cameltask.processor.OnlineSalesChannelFilter;
import com.example.cameltask.processor.OrderToDatabaseProcessor;
import com.example.cameltask.strategy.CountryAggregationStrategy;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyRoute extends RouteBuilder {

    //private final CurrentTime currentTime;
    private final OrderToDatabaseProcessor orderToDatabaseProcessor;

    @Override
    public void configure() throws Exception {


        from("file:in")//?noop=true
                // csv -> IncomingOrder
                .unmarshal()
                .bindy(BindyType.Csv, IncomingOrder.class)
                .split(body())
                // drop all offline orders
                .filter().method(OnlineSalesChannelFilter.class, "isOnline")
                //.process(orderToDatabaseProcessor)
                .process(exchange -> {
                    Message message = exchange.getMessage();
                    IncomingOrder incomingOrder = message.getBody(IncomingOrder.class);
                    message.setHeader("country", incomingOrder.getCountry());
                    message.setHeader("region", incomingOrder.getRegion());
                })
                .aggregate(header("country"), new CountryAggregationStrategy())
                .completionTimeout(1000)
                .log("${body}");
        //todo: Aggregate orders by Region and produce CSV report for each region in form:
        // process set header country and region
        // aggregate

        // Country,OrderCount,AverageUnitsSold,AverageUnitPrice,AverageUnitCost,TotalRevenue,TotalCost,TotalProfit
//,where TotalRevenue,TotalCost,TotalProfit is in Millions
//                ,CSV report should be generated to local disk, folder ‘out/reports’
//    ,CSV report file name should be in a form <Region>_<CurrentTimestamp>.csv


        //.to("mock:out");
        //.to("mock:result");
        //.to("mock:end");
        //.to("file:out");

//        from("timer:first-timer")
//                .bean("currentTime")
//                .process(new MyLoggingProcessor())
//                .to("log:first-timer");
    }
}
