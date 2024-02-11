package com.example.CommodityViewer.Commodity;


import java.util.List;

public class CommodityDto {
    private String name;
    private List<List<Object>> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<Object>> getData() {
        return data;
    }

    public void setData(List<List<Object>> data) {
        this.data = data;
    }
}



