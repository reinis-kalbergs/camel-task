package com.example.cameltask.processor;

import com.example.cameltask.model.CountryData;
import com.example.cameltask.model.database.RegionReportEntity;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class RegionReportToEntityProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        CountryData countryData = exchange.getMessage().getBody(CountryData.class);
        RegionReportEntity regionReport = new RegionReportEntity(countryData);

        String fileName = (String) exchange.getMessage().getHeader("custom-file-name");
        addInfoFromFileName(regionReport, fileName);

        exchange.getMessage().setBody(regionReport);
    }

    private void addInfoFromFileName(RegionReportEntity regionReport, String fileName) {
        String[] splitFileName = fileName.split("_");
        splitFileName[1] = splitFileName[1].replace(".csv", "");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
        LocalDateTime timeStamp = LocalDateTime.parse(splitFileName[1], formatter);

        regionReport.setRegion(splitFileName[0]);
        regionReport.setCsvFileName(fileName);
        regionReport.setProcessingDate(timeStamp);
    }
}
