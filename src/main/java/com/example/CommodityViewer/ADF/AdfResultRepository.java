package com.example.CommodityViewer.ADF;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdfResultRepository extends JpaRepository<AdfResult, Long> {
    List<AdfResult> findByCommodityId(Long commodityId);
}
