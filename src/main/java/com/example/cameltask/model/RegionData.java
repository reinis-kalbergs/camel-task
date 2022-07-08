package com.example.cameltask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionData {
    private List<CountryData> regionData = new ArrayList<>();

    public void addCountryData(CountryData countryData) {
        regionData.add(countryData);
    }
}
