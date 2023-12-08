package com.example.CommodityViewer.Commodity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommodityService {
    @Autowired
    private CommodityRepository commodityRepository;
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
    public Map<String, List<DataPointEntity>> getSelectedCommoditiesData(String commodityName1, String commodityName2) {
        Map<String, List<DataPointEntity>> selectedCommoditiesData = new HashMap<>();

        Optional<CommodityEntity> commodityOptional1 = commodityRepository.findByName(commodityName1);
        Optional<CommodityEntity> commodityOptional2 = commodityRepository.findByName(commodityName2);

        commodityOptional1.ifPresent(commodity1 -> selectedCommoditiesData.put(commodityName1, commodity1.getDataPoints()));
        commodityOptional2.ifPresent(commodity2 -> selectedCommoditiesData.put(commodityName2, commodity2.getDataPoints()));

        return selectedCommoditiesData;
    }


}