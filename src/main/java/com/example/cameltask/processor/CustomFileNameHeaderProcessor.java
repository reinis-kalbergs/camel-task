package com.example.cameltask.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CustomFileNameHeaderProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        String customFileName = exchange.getMessage().getHeader("region").toString()
                + "_" + getNow() + ".csv";
        exchange.getMessage().setHeader("custom-file-name", customFileName);
    }

    private String getNow() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
        return LocalDateTime.now().format(formatter);
    }
}
