package com.example.CommodityViewer.Commodity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DataPointRepository extends JpaRepository<DataPointEntity, Long> {


    List<DataPointEntity> findByCommodity(CommodityEntity commodity);

    DataPointEntity findTopByCommodityOrderByTimestampDesc(Optional<CommodityEntity> commodity);

    boolean existsByCommodityAndTimestamp(CommodityEntity commodity, Date timestamp);
}