package com.example.demo.Commodity;

public enum CommodityType {
    ROPA("baryłka", "USD"),
    ZLOTO("uncja", "USD"),
    MIEDZ("funt", "USD"),
    SREBRO("uncja", "USD"),
    PLATYNA("uncja", "USD"),
    NIKIEL("funt", "USD"),
    ALUMINIUM("tona", "USD"),
    CUKIER("funt", "USc"),
    PSZENICA("korzec", "USc"),
    SOJA("korzec", "USc"),
    PALLAD("uncja", "USD"),
    RZEPAK("tona", "USD"),
    KAWA("funt", "USc");

    private String unit;
    private String currency;

    CommodityType() {
    }

    CommodityType(String unit, String currency) {
        this.unit = unit;
        this.currency = currency;
    }

    public String getUnit() {
        return unit;
    }

    public String getCurrency() {
        return currency;
    }
}

