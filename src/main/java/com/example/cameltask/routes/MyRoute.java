package com.example.cameltask.routes;

import com.example.cameltask.model.CountryData;
import com.example.cameltask.model.IncomingOrder;
import com.example.cameltask.model.RegionData;
import com.example.cameltask.other.OnlineSalesChannelFilter;
import com.example.cameltask.processor.CountryDataProcessor;
import com.example.cameltask.processor.NewHeaderProcessor;
import com.example.cameltask.processor.RegionReportProcessor;
import com.example.cameltask.processor.database.OrderToDatabaseProcessor;
import com.example.cameltask.processor.database.RegionReportToDatabaseProcessor;
import com.example.cameltask.strategy.CountryAggregationStrategy;
import com.example.cameltask.strategy.RegionAggregationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MyRoute extends RouteBuilder {
    //@formatter:off
    private final OrderToDatabaseProcessor orderToDatabaseProcessor;
    private final RegionReportToDatabaseProcessor regionReportToDatabaseProcessor;
    private final NewHeaderProcessor newHeaderProcessor;

    @Value("${camel-task.headers.country}")
    private String COUNTRY_HEADER;
    @Value("${camel-task.headers.region}")
    private String REGION_HEADER;

    @Override
    public void configure() throws Exception {

        from("file:in?noop=true")
                .unmarshal()
                    .bindy(BindyType.Csv, IncomingOrder.class)
                .split(body())
                .filter()
                    .method(OnlineSalesChannelFilter.class, "isOnline")
                .process(orderToDatabaseProcessor)

                .to("direct:aggregate-region-report");

        from("direct:aggregate-region-report")
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
                .to("direct:region-report-csv");

        from("direct:region-report-csv")
                .process(exchange -> {
                    //get List from Object
                    RegionData regionData = exchange.getMessage().getBody(RegionData.class);
                    exchange.getMessage().setBody(regionData.getRegionData());
                })
                .marshal()
                    .bindy(BindyType.Csv, CountryData.class)
                .to("file:out/reports?fileName=${header.region}_${date:now:yyyy-MM-dd HH.mm.ss}.csv");

        from("file:out/reports?noop=true")
                .unmarshal()
                    .bindy(BindyType.Csv, CountryData.class)
                .split(body())
                .process(new RegionReportProcessor())
                .process(regionReportToDatabaseProcessor)
                .end();
    }
}
