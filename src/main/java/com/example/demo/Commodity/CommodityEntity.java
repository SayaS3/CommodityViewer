package com.example.demo.Commodity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "commodities")
public class CommodityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name") // This annotation is used to specify the column name in the database
    private String name;
    @OneToMany(mappedBy = "commodity")
    private List<DataPointEntity> dataPoints;

    public List<DataPointEntity> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPointEntity> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
