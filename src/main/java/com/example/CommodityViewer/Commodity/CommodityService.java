package com.example.CommodityViewer.Commodity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommodityService {
    @Autowired
    private CommodityRepository commodityRepository;
    public Optional<CommodityEntity> findCommodityByName(String commodityName) {
        return commodityRepository.findByName(commodityName);
    }
    public List<DataPointEntity> getDataPointsForCommodity(String commodityName) {
        Optional<CommodityEntity> commodityOptional = commodityRepository.findByName(commodityName);
        return commodityOptional.map(CommodityEntity::getDataPoints).orElse(Collections.emptyList());
    }


}