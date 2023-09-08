package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommodityDataService {
@Autowired
    private CommodityDataRepository commodityDataRepository;
    public CommodityData save(CommodityData commodityData) {
        return commodityDataRepository.save(commodityData);
    }
    public boolean existsByType(CommodityType type) {
        return commodityDataRepository.existsByCommodityType(type);
    }

    public Optional<CommodityData> findByType(CommodityType type) {
        return commodityDataRepository.findByCommodityType(type);
    }
}
