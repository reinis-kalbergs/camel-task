package com.example.cameltask.processor;

import com.example.cameltask.model.IncomingOrder;
import com.example.cameltask.model.Order;
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
        orderRepository.save(new Order(exchange.getMessage().getBody(IncomingOrder.class)));
    }


}
