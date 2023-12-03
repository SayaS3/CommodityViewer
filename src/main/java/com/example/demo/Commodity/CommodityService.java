package com.example.demo.Commodity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CommodityService {
    @Autowired
    private CommodityRepository commodityRepository;
    private DataPointRepository dataPointRepository;
    public CommodityEntity save(CommodityEntity commodityData) {
        return commodityRepository.save(commodityData);
    }
    public Optional<CommodityEntity> findCommodityByName(String commodityName) {
        return commodityRepository.findByName(commodityName);
    }
    public List<DataPointEntity> getDataPointsForCommodity(String commodityName) {
        Optional<CommodityEntity> commodityOptional = commodityRepository.findByName(commodityName);
        return commodityOptional.map(CommodityEntity::getDataPoints).orElse(Collections.emptyList());
    }


}