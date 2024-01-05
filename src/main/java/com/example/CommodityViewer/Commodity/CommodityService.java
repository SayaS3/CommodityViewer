package com.example.CommodityViewer.Commodity;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommodityService {
    @Autowired
    private CommodityRepository commodityRepository;
    public Optional<CommodityEntity> findCommodityByName(String commodityName) {
        return commodityRepository.findByName(commodityName);
    }
    public List<DataPointEntity> getDataPointsForCommodity(String commodityName) {
        Optional<CommodityEntity> commodityOptional = commodityRepository.findByName(commodityName);
        return commodityOptional.map(CommodityEntity::getDataPoints).orElse(Collections.emptyList());
    }
    public double calculatePearsonCorrelation(List<Double> values1, List<Double> values2) {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();

        // Znajdź wspólne daty
        List<Double> commonValues1 = new ArrayList<>();
        List<Double> commonValues2 = new ArrayList<>();
        for (int i = 0; i < values1.size(); i++) {
            if (values2.size() > i) {
                commonValues1.add(values1.get(i));
                commonValues2.add(values2.get(i));
            }
        }

        double[] array1 = commonValues1.stream().mapToDouble(Double::doubleValue).toArray();
        double[] array2 = commonValues2.stream().mapToDouble(Double::doubleValue).toArray();

        return pearsonsCorrelation.correlation(array1, array2);
    }

    // Metoda do obliczania współczynnika korelacji Spearmana
    public double calculateSpearmanCorrelation(List<Double> values1, List<Double> values2) {
        SpearmansCorrelation spearmansCorrelation = new SpearmansCorrelation();

        // Znajdź wspólne daty
        List<Double> commonValues1 = new ArrayList<>();
        List<Double> commonValues2 = new ArrayList<>();
        for (int i = 0; i < values1.size(); i++) {
            if (values2.size() > i) {
                commonValues1.add(values1.get(i));
                commonValues2.add(values2.get(i));
            }
        }

        double[] array1 = commonValues1.stream().mapToDouble(Double::doubleValue).toArray();
        double[] array2 = commonValues2.stream().mapToDouble(Double::doubleValue).toArray();

        return spearmansCorrelation.correlation(array1, array2);
    }
    private List<String> getCommodityTypesList() {
        return Arrays.stream(CommodityType.values())
                .map(CommodityType::name)
                .collect(Collectors.toList());
    }
}