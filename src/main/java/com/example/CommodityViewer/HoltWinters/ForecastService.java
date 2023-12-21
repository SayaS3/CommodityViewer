package com.example.CommodityViewer.HoltWinters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForecastService {

    @Autowired
    private ForecastRepository forecastRepository;

    public List<Forecast> getForecastsByCommodityId(Long commodityId) {
        return forecastRepository.findByCommodityId(commodityId);
    }
}