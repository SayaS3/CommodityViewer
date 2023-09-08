package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;


@Entity
@Table(name = "DATA_POINT")
public class DataPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("date")
    private String date;
    @JsonProperty("value")
    private String value_column;

    public DataPoint() {
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue_column() {
        return value_column;
    }

    public void setValue_column(String value) {
        this.value_column = value;
    }
}

