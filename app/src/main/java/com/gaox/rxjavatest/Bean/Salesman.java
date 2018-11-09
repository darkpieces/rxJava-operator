package com.gaox.rxjavatest.Bean;

/**
 * @author: gaox
 * @date: 2018/11/05 17:01
 */
public class Salesman {

    private long cityStoreId;
    private long salesManId;
    private long salesPerformance;


    public long getCityStoreId() {
        return cityStoreId;
    }

    public void setCityStoreId(long cityStoreId) {
        this.cityStoreId = cityStoreId;
    }

    public long getSalesManId() {
        return salesManId;
    }

    public void setSalesManId(long salesManId) {
        this.salesManId = salesManId;
    }

    public long getSalesPerformance() {
        return salesPerformance;
    }

    public void setSalesPerformance(long salesPerformance) {
        this.salesPerformance = salesPerformance;
    }

    @Override
    public String toString() {
        return "Salesman{" +
                "cityStoreId=" + cityStoreId +
                ", salesPerformance=" + salesPerformance +
                '}';
    }
}

