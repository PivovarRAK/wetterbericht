package com.example.weathercastapp;

public class formeln {

    public static double ktoc(double k){
        k = k - 273.15;
        k = Math.round(k * 100) / 100.0;
        return k;
    }
    public static double mstokmh(double ms){
        ms = ms*3.6;
        ms = Math.round(ms * 100) / 100.0;
        return ms;
    }
}
