package com.example.demo.Commodity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommodityRepository extends JpaRepository<CommodityEntity, Long> {

        Optional<CommodityEntity> findByName(String commodityType);
}
