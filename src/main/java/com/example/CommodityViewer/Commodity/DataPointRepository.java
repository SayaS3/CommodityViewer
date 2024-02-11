package com.example.CommodityViewer.Commodity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DataPointRepository extends JpaRepository<DataPointEntity, Long> {


    List<DataPointEntity> findByCommodity(Commodity commodity);

    DataPointEntity findTopByCommodityOrderByTimestampDesc(Optional<Commodity> commodity);


}