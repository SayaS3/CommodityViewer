package com.example.CommodityViewer;


import com.example.CommodityViewer.Commodity.DataPoint;

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


}