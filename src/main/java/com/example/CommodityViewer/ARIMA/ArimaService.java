package com.example.CommodityViewer.ARIMA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ArimaService {
    @Autowired
    private ArimaRepository arimaRepository;
   public List<Arima> getArimabyCommodityId(Long id){
        return arimaRepository.findByCommodityId(id);
    }
}
