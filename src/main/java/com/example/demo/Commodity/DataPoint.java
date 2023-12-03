package com.example.demo.Commodity;


import java.util.Date;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class DataPoint {
    private Date timestamp;
    private Double value;

    public DataPoint() {
    }

    public Date getTimestamp() {
        return timestamp;
    }
    public DataPoint(Date timestamp, Double value) {
        this.timestamp = timestamp;
        this.value = value;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return "DataPointEntity{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }
}

