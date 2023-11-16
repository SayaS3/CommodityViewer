package com.example.demo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AlphaVantageService {

    @Value("${API_KEY}")  // Set default value to "demo" if the environment variable is not present
    private String apiKey;


    private final String ALPHA_VANTAGE_BASE_URL = "https://www.alphavantage.co/query?function=";

    public Commodity fetchDataForCommodity(CommodityType commodityType) {
        String fullUrl = buildUrlForCommodity(commodityType);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ApiResponse> response = restTemplate.getForEntity(fullUrl, ApiResponse.class);

        ApiResponse apiResponse = response.getBody();

        Commodity commodity = new Commodity();
        commodity.setName(apiResponse.getName());
        commodity.setInterval_column(apiResponse.getInterval_column());
        commodity.setUnit(apiResponse.getUnit());
        commodity.setData(apiResponse.getData());
        commodity.setCOMMODITY_TYPE(commodityType.name());


        return commodity;
    }

    private String buildUrlForCommodity(CommodityType commodityType) {
        return ALPHA_VANTAGE_BASE_URL + commodityType.name() + "&interval=monthly&apikey=" + apiKey;
    }
}




