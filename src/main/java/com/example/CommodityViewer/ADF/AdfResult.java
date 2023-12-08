package com.example.CommodityViewer.ADF;

import jakarta.persistence.*;

@Entity
@Table(name = "adf_results")
public class AdfResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "commodity_id")
    private Long commodityId;

    @Column(name = "adf_statistic")
    private Double adfStatistic;

    @Column(name = "p_value")
    private Double pValue;

    @Column(name = "critical_value_1_percent")
    private Double criticalValue1Percent;

    @Column(name = "critical_value_5_percent")
    private Double criticalValue5Percent;

    @Column(name = "critical_value_10_percent")
    private Double criticalValue10Percent;

    @Column(name = "is_stationary")
    private Integer isStationary;

    public AdfResult() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public Double getAdfStatistic() {
        return adfStatistic;
    }

    public void setAdfStatistic(Double adfStatistic) {
        this.adfStatistic = adfStatistic;
    }

    public Double getpValue() {
        return pValue;
    }

    public void setpValue(Double pValue) {
        this.pValue = pValue;
    }

    public Double getCriticalValue1Percent() {
        return criticalValue1Percent;
    }

    public void setCriticalValue1Percent(Double criticalValue1Percent) {
        this.criticalValue1Percent = criticalValue1Percent;
    }

    public Double getCriticalValue5Percent() {
        return criticalValue5Percent;
    }

    public void setCriticalValue5Percent(Double criticalValue5Percent) {
        this.criticalValue5Percent = criticalValue5Percent;
    }

    public Double getCriticalValue10Percent() {
        return criticalValue10Percent;
    }

    public void setCriticalValue10Percent(Double criticalValue10Percent) {
        this.criticalValue10Percent = criticalValue10Percent;
    }

    public Integer getIsStationary() {
        return isStationary;
    }

    public void setIsStationary(Integer isStationary) {
        this.isStationary = isStationary;
    }
}
