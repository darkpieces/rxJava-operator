package com.gaox.rxjavatest.Bean;

/**
 * @author: gaox
 * @date: 2018/11/08 15:27
 */
public class DistinctBean {
    public String str;

    public DistinctBean(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "DistinctBean{" +
                "str='" + str + '\'' +
                '}';
    }
}
