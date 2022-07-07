package com.example.cameltask.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Slf4j
public class MyLoggingProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("meTestingProcessor"+exchange.getMessage().getBody());
    }
}
