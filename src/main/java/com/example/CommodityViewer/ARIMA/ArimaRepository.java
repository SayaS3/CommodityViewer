package com.example.CommodityViewer.ARIMA;

import com.example.CommodityViewer.HoltWinters.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArimaRepository extends JpaRepository<Arima, Long> {
    List<Arima> findByCommodityId(Long commodityId);
}
