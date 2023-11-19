package com.example.demo.Commodity;


import java.util.List;

public class Commodity {
    private String name;
    private String COMMODITY_TYPE;
    private String interval_column;
    private String unit;
    private List<DataPoint> data;


    public Commodity() {
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

    public String getCOMMODITY_TYPE() {
        return COMMODITY_TYPE;
    }

    public void setCOMMODITY_TYPE(String COMMODITY_TYPE) {
        this.COMMODITY_TYPE = COMMODITY_TYPE;
    }
}

