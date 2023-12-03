package com.example.demo;


import com.example.demo.Commodity.DataPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApiResponse {
    private List<List<Number>> main;

    public List<List<Number>> getMain() {
        return main;
    }

    public void setMain(List<List<Number>> main) {
        this.main = main;
    }

    public List<DataPoint> getDataPoints() {
        List<DataPoint> dataPoints = new ArrayList<>();
        for (List<Number> entry : main) {
            if (entry.size() == 2) {
                dataPoints.add(new DataPoint(new Date(entry.get(0).longValue()), entry.get(1).doubleValue()));
            }
        }
        return dataPoints;
    }
}