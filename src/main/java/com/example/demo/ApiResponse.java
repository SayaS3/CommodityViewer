package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ApiResponse {
    @JsonProperty("name")
    private String name;
    @JsonProperty("interval")
    private String interval_column;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("data")
    private List<DataPoint> data;

    public ApiResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInterval_column() {
        return interval_column;
    }

    public void setInterval_column(String interval_column) {
        this.interval_column = interval_column;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<DataPoint> getData() {
        return data;
    }

    public void setData(List<DataPoint> data) {
        this.data = data;
    }
}

