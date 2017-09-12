package com.mazmellow.testomise.model;

/**
 * Created by maz on 9/12/2017 AD.
 */

public class RequestModel {
    private String name, token;
    private int amount;

    public void setName(String name) {
        this.name = name;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
