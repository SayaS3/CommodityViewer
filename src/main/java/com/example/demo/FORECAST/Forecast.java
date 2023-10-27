package com.example.demo.FORECAST;

import jakarta.persistence.*;

import java.time.LocalDate;

// Forecast.java
// Forecast.java
// Forecast.java
@Entity
@Table(name = "forecasts")
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "commodity_id")
    private Long commodityId;

    @Column(name = "forecast_date")
    private LocalDate forecastDate;

    @Column(name = "forecast_value")
    private Double forecastValue;

    public Forecast() {
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

    public LocalDate getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(LocalDate forecastDate) {
        this.forecastDate = forecastDate;
    }

    public Double getForecastValue() {
        return forecastValue;
    }

    public void setForecastValue(Double forecastValue) {
        this.forecastValue = forecastValue;
    }
}



// AdfResult.java