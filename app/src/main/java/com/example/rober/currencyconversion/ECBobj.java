package com.example.rober.currencyconversion;

import java.util.HashMap;

public class ECBobj {
    public String success;
    public int timestamp;
    public String base;
    public String date;
    public HashMap<String, Double> rates;
    public ECBobj(String s, int ts, String b, String d, HashMap<String, Double> r) {
        success = s;
        timestamp = ts;
        base = b;
        date = d;
        rates = r;
    }
}
