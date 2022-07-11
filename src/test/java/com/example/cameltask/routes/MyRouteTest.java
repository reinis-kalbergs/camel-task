package com.example.cameltask.routes;

import com.example.cameltask.model.IncomingOrder;
import com.example.cameltask.model.database.OrderEntity;
import com.example.cameltask.repository.OrderRepository;
import com.example.cameltask.repository.RegionReportRepository;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@CamelSpringBootTest
@MockEndpoints("direct:save-order-to-database")
public class MyRouteTest {

    @Autowired
    ProducerTemplate template;

    @EndpointInject("mock:save-order-to-database")
    MockEndpoint mockSaveToDb;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RegionReportRepository regionReportRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        regionReportRepository.deleteAll();
    }

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
        IncomingOrder incomingOrder = new IncomingOrder(
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

        template.sendBody("direct:save-order-to-database", incomingOrder);

        List<OrderEntity> result = orderRepository.findAll();

        Assertions.assertThat(result).hasSize(1);
        OrderEntity actualResult = result.get(0);

        OrderEntity expectedResult = new OrderEntity(incomingOrder);
        expectedResult.setId(1L);

        Assertions.assertThat(expectedResult).isEqualTo(actualResult);
    }
}
