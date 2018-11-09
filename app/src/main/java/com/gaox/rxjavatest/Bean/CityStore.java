package com.gaox.rxjavatest.Bean;

import java.util.List;

/**
 * @author: gaox
 * @date: 2018/11/05 17:00
 */
public class CityStore {

    private long cityCtoreId;

    private List<Salesman> salesman;

    public List<Salesman> getSalesman() {
        return salesman;
    }

    public long getCityCtoreId() {
        return cityCtoreId;
    }

    public void setCityCtoreId(long cityCtoreId) {
        this.cityCtoreId = cityCtoreId;
    }

    public void setSalesman(List<Salesman> salesman) {
        this.salesman = salesman;
    }

    @Override
    public String toString() {
        return "CityStore{" +
                "cityCtoreId=" + cityCtoreId +
                ", salesman=" + salesman +
                '}';
    }
}
