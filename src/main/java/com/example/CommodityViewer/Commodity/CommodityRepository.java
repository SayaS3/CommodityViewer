package com.example.CommodityViewer.Commodity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommodityRepository extends JpaRepository<Commodity, Long> {
        Optional<Commodity> findByName(String commodityType);
}
