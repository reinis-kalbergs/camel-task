package com.example.cameltask.processor.database;

import com.example.cameltask.model.database.RegionReportEntity;
import com.example.cameltask.repository.RegionReportRepository;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RegionReportToDatabaseProcessor implements Processor {

    private final RegionReportRepository regionReportRepository;

    @Override
    public void process(Exchange exchange) throws Exception {
        regionReportRepository.save(exchange.getMessage().getBody(RegionReportEntity.class));
    }
}
