package com.example.cameltask.routes;

import com.example.cameltask.model.CountryData;
import com.example.cameltask.model.IncomingOrder;
import com.example.cameltask.model.RegionAggregateData;
import com.example.cameltask.model.database.OrderEntity;
import com.example.cameltask.model.database.RegionReportEntity;
import com.example.cameltask.repository.OrderRepository;
import com.example.cameltask.repository.RegionReportRepository;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@CamelSpringBootTest
@MockEndpoints
public class MyRouteTest {

    @Autowired
    ProducerTemplate template;

    @EndpointInject("mock:direct:save-order-to-database")
    MockEndpoint mockSaveToDb;

    @EndpointInject("mock:direct:aggregate-region-report")
    MockEndpoint mockAggregateRegionReportCsv;

    @EndpointInject("mock:direct:create-region-report-csv")
    MockEndpoint mockCreateRegionReportCsv;

    @EndpointInject("mock:file:out/reports")
    MockEndpoint mockWriteRegionReportCsv;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RegionReportRepository regionReportRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        regionReportRepository.deleteAll();
    }

    final IncomingOrder INCOMING_ORDER_1 = new IncomingOrder(
            "North America", "Canada", "Fruits", "Online", "H",
            LocalDate.of(2013, 11, 15),
            137209212L,
            LocalDate.of(2013, 12, 19),
            2110L,
            new BigDecimal("9.33"),
            new BigDecimal("6.92"),
            new BigDecimal("19686.30"),
            new BigDecimal("14601.20"),
            new BigDecimal("5085.10"));

    final IncomingOrder INCOMING_ORDER_2 = new IncomingOrder(
            "North America", "Canada", "Cosmetics", "Online", "H",
            LocalDate.of(2013, 11, 15),
            572010314L,
            LocalDate.of(2013, 12, 19),
            9418L,
            new BigDecimal("437.20"),
            new BigDecimal("263.33"),
            new BigDecimal("4117549.60"),
            new BigDecimal("2480041.94"),
            new BigDecimal("1637507.66"));

    final IncomingOrder INCOMING_ORDER_3 = new IncomingOrder(
            "North America", "USA", "Cosmetics", "Online", "H",
            LocalDate.of(2013, 11, 15),
            572010314L,
            LocalDate.of(2013, 12, 19),
            9418L,
            new BigDecimal("437.20"),
            new BigDecimal("263.33"),
            new BigDecimal("4117549.60"),
            new BigDecimal("2480041.94"),
            new BigDecimal("1637507.66"));

    final CountryData COUNTRY_DATA_1 = CountryData.builder()
            .country("USA")
            .orderCount(1L)
            .averageUnitsSold(new BigDecimal("9418"))
            .averageUnitPrice(new BigDecimal("437.20"))
            .averageUnitCost(new BigDecimal("263.33"))
            .totalRevenue(new BigDecimal("4.12"))
            .totalCost(new BigDecimal("2.48"))
            .totalProfit(new BigDecimal("1.64")).build();

    final CountryData COUNTRY_DATA_2 = CountryData.builder()
            .country("Canada")
            .orderCount(4L)
            .averageUnitsSold(new BigDecimal("3937"))
            .averageUnitPrice(new BigDecimal("116.30"))
            .averageUnitCost(new BigDecimal("71.02"))
            .totalRevenue(new BigDecimal("4.18"))
            .totalCost(new BigDecimal("2.52"))
            .totalProfit(new BigDecimal("1.65")).build();

    @Test
    void shouldConvertToObjectAndFilterOnlineOrders() throws InterruptedException {

        mockSaveToDb.expectedBodiesReceived(
                "IncomingOrder(region=North America, country=Canada, itemType=Fruits, salesChannel=Online, orderPriority=H, orderDate=2013-11-15, orderId=137209212, shipDate=2013-12-29, unitsSold=2110, unitPrice=9.33, unitCost=6.92, totalRevenue=19686.30, totalCost=14601.20, totalProfit=5085.10)"
        );

        template.sendBody("file:in?noop=true",
                "Region,Country,Item Type,Sales Channel,Order Priority,Order Date,Order ID,Ship Date,Units Sold,Unit Price,Unit Cost,Total Revenue,Total Cost,Total Profit\n" +
                        "North America,Canada,Fruits,Online,H,11/15/2013,137209212,12/29/2013,2110,9.33,6.92,19686.30,14601.20,5085.10\n" +
                        "Europe,Italy,Fruits,Offline,C,7/16/2016,148573625,7/18/2016,5092,9.33,6.92,47508.36,35236.64,12271.72"
        );

        mockSaveToDb.assertIsSatisfied();
    }


    @Test
    void shouldAddIncomingOrderToDatabase() {

        template.sendBody("direct:save-order-to-database", INCOMING_ORDER_1);

        List<OrderEntity> result = orderRepository.findAll();

        Assertions.assertThat(result).hasSize(1);
        OrderEntity actualResult = result.get(0);

        OrderEntity expectedResult = new OrderEntity(INCOMING_ORDER_1);
        expectedResult.setId(1L);

        Assertions.assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void shouldAddHeaders() throws InterruptedException {

        template.sendBody("direct:aggregate-region-report", INCOMING_ORDER_1);

        mockCreateRegionReportCsv.expectedHeaderReceived("region", "North America");
        mockCreateRegionReportCsv.expectedHeaderReceived("country", "Canada");
        mockCreateRegionReportCsv.expectedMessageCount(1);

        mockCreateRegionReportCsv.assertIsSatisfied();
    }

    @Test
    void shouldAggregateByRegions() throws InterruptedException {

        RegionAggregateData expectedResult = new RegionAggregateData(List.of(COUNTRY_DATA_1, COUNTRY_DATA_2));

        template.sendBody("direct:aggregate-region-report", INCOMING_ORDER_1);
        template.sendBody("direct:aggregate-region-report", INCOMING_ORDER_2);
        template.sendBody("direct:aggregate-region-report", INCOMING_ORDER_3);

        mockCreateRegionReportCsv.expectedMessageCount(1);
        mockCreateRegionReportCsv.expectedBodiesReceived(expectedResult);

        mockCreateRegionReportCsv.assertIsSatisfied();
    }

    @Test
    void shouldWriteRegionReportToCsv() throws InterruptedException {
        RegionAggregateData expectedResult = new RegionAggregateData(List.of(COUNTRY_DATA_1, COUNTRY_DATA_2));

        template.sendBody("direct:create-region-report-csv", expectedResult);

        mockWriteRegionReportCsv.expectedBodiesReceived(
                "country,orderCount,averageUnitsSold,averageUnitPrice,averageUnitCost,totalRevenue,totalCost,totalProfit\n" +
                        "USA,1,9418,437.20,263.33,4.12,2.48,1.64\n" +
                        "Canada,4,3937,116.30,71.02,4.18,2.52,1.65"
        );
        mockWriteRegionReportCsv.assertIsSatisfied();
    }

    @Test
    void shouldSaveRegionReportToDatabase() throws InterruptedException {
        String fileName = "North America_1999-11-11 11.11.11.csv";
        template.sendBodyAndHeader(
                "file:out/reports",
                "country,orderCount,averageUnitsSold,averageUnitPrice,averageUnitCost,totalRevenue,totalCost,totalProfit\n" +
                        "USA,1,9418,437.20,263.33,4.12,2.48,1.64\n" +
                        "Canada,4,3937,116.30,71.02,4.18,2.52,1.65",
                "CamelFileName",
                fileName
        );

        RegionReportEntity expectedRegionReport1 = RegionReportEntity.builder()
                .country("USA")
                .orderCount(1L)
                .averageUnitsSold(new BigDecimal("9418.00"))
                .averageUnitPrice(new BigDecimal("437.20"))
                .averageUnitCost(new BigDecimal("263.33"))
                .totalRevenue(new BigDecimal("4.12"))
                .totalCost(new BigDecimal("2.48"))
                .totalProfit(new BigDecimal("1.64"))
                .csvFileName(fileName)
                .id(1L)
                .region("North America")
                .processingDate(LocalDateTime.parse("1999-11-11T11:11:11"))
                .build();
        RegionReportEntity expectedRegionReport2 = RegionReportEntity.builder()
                .country("Canada")
                .orderCount(4L)
                .averageUnitsSold(new BigDecimal("3937.00"))
                .averageUnitPrice(new BigDecimal("116.30"))
                .averageUnitCost(new BigDecimal("71.02"))
                .totalRevenue(new BigDecimal("4.18"))
                .totalCost(new BigDecimal("2.52"))
                .totalProfit(new BigDecimal("1.65"))
                .csvFileName(fileName)
                .id(2L)
                .region("North America")
                .processingDate(LocalDateTime.parse("1999-11-11T11:11:11"))
                .build();

        Thread.sleep(3000);

        List<RegionReportEntity> regionReports = regionReportRepository.findAll();

        Assertions.assertThat(regionReports).containsAll(List.of(expectedRegionReport1, expectedRegionReport2));
        Assertions.assertThat(regionReports).hasSize(2);
    }
}
