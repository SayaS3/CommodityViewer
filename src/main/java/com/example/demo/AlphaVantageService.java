package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AlphaVantageService {

    @Value("${alpha.vantage.api.key}")
    private String apiKey;

    private final String ALPHA_VANTAGE_BASE_URL = "https://www.alphavantage.co/query?function=";

    public Commodity fetchDataForCommodity(CommodityType commodityType) {
        String fullUrl = buildUrlForCommodity(commodityType);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ApiResponse> response = restTemplate.getForEntity(fullUrl, ApiResponse.class);

        ApiResponse apiResponse = response.getBody();

        Commodity commodity = new Commodity();
        commodity.setName(apiResponse.getName());
        commodity.setInterval(apiResponse.getInterval());
        commodity.setUnit(apiResponse.getUnit());
        commodity.setData(apiResponse.getData());


        return commodity;
    }

    private String buildUrlForCommodity(CommodityType commodityType) {
        return ALPHA_VANTAGE_BASE_URL + commodityType.name() + "&interval=monthly&apikey=" + apiKey;
    }
}




