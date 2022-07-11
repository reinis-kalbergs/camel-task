package com.example.cameltask.processor.database;

import com.example.cameltask.model.IncomingOrder;
import com.example.cameltask.model.database.OrderEntity;
import com.example.cameltask.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderToDatabaseProcessor implements Processor {

    private final OrderRepository orderRepository;

    @Override
    public void process(Exchange exchange) throws Exception {
        orderRepository.save(new OrderEntity(exchange.getMessage().getBody(IncomingOrder.class)));
    }
}
