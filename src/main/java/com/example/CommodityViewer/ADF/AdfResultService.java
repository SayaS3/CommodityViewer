package com.example.CommodityViewer.ADF;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdfResultService {

    @Autowired
    private AdfResultRepository adfResultRepository;

    public List<AdfResult> getAdfResultsByCommodityId(Long commodityId) {
        return adfResultRepository.findByCommodityId(commodityId);
    }
}
