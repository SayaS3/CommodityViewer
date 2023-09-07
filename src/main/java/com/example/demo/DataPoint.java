package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;



public class DataPoint {
    @JsonProperty("date")
    private String date;
    @JsonProperty("value")
    private String value;

    public DataPoint() {
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

