package com.example.cameltask.routes;

import com.example.cameltask.filter.OnlineSalesChannelFilter;
import com.example.cameltask.model.CountryData;
import com.example.cameltask.model.IncomingOrder;
import com.example.cameltask.processor.*;
import com.example.cameltask.processor.database.OrderToDatabaseProcessor;
import com.example.cameltask.processor.database.RegionReportToDatabaseProcessor;
import com.example.cameltask.strategy.CountryAggregationStrategy;
import com.example.cameltask.strategy.RegionAggregationStrategy;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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

        from("file:in?delete=true")
                .unmarshal()
                    .bindy(BindyType.Csv, IncomingOrder.class)
                .split(body())
                .filter()
                    .method(OnlineSalesChannelFilter.class, "isOnline")
                    .to("direct:save-order-to-database","direct:aggregate-region-report");

        from("direct:save-order-to-database")
                .process(orderToDatabaseProcessor);

        from("direct:aggregate-region-report")
                .process(newHeaderProcessor)
                .aggregate(header(COUNTRY_HEADER), new CountryAggregationStrategy())
                    .completionTimeout(1000)
                .process(new CountryAverageDataProcessor())
                .aggregate(header(REGION_HEADER), new RegionAggregationStrategy())
                    .completionTimeout(1000)
                .process(new RegionDataToListProcessor())
                .process(new CustomFileNameHeaderProcessor())
                .multicast()
                    .to("direct:create-region-report-csv","direct:save-region-report-to-database");

        from("direct:create-region-report-csv")
                .marshal()
                    .bindy(BindyType.Csv, CountryData.class)
                .to("file:out/reports?fileName=${header.custom-file-name}");

        from("direct:save-region-report-to-database")
                .split(body())
                .process(new RegionReportProcessor())
                .process(regionReportToDatabaseProcessor);
    }
}
