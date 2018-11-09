package com.gaox.rxjavatest.net;

import java.io.Serializable;

/**
 * Created by gaox on 2017/3/11.
 */

public class BaseDataResponse<T> implements Serializable {

    public static final int SUCCESS = 200;

    private int httpCode;
    private String msg;
    private String timestamp;
    private T data;
    private int current;
    private int size;
    private int pages;
    private int total;
    private String ticket;
    private String phone;
    private String draw;

    public int getHttpCode() {
        return httpCode;
    }

    public String getMsg() {
        return msg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public T getData() {
        return data;
    }

    public int getCurrent() {
        return current;
    }

    public int getSize() {
        return size;
    }

    public int getPages() {
        return pages;
    }

    public int getTotal() {
        return total;
    }

    public String getTicket() {
        return ticket;
    }

    public String getPhone() {
        return phone;
    }
}
