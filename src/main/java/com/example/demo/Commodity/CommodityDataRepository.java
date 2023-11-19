package com.example.demo.Commodity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommodityDataRepository extends JpaRepository<CommodityData, Long> {
    boolean existsByCommodityType(CommodityType commodityType);
    Optional<CommodityData> findByCommodityType(CommodityType commodityType);

}

