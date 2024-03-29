package com.example.CommodityViewer.Commodity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "data_points")
public class DataPointEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
    @ManyToOne
    @JoinColumn(name = "commodity_id")
    private Commodity commodity;

    @Column(name = "timestamp")
    private Date timestamp;
    @Column(name = "value")
    private Double value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getCommodityName() {
        return commodity != null ? commodity.getName() : null;
    }
    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public Date getTimestamp() {
        return timestamp;
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
                ", commodity=" + commodity +
                ", timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }
}
