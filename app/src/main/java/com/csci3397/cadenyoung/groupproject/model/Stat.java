package com.csci3397.cadenyoung.groupproject.model;

public class Stat {
    private int imageId;
    private String name;
    private int descId;
    private double value;

    public Stat(int imageId, String name, int descId) {
        this.imageId = imageId;
        this.name = name;
        this.descId = descId;
        this.value = 0;
    }

    public Stat(int imageId, String name, int desc, double value) {
        this.imageId = imageId;
        this.name = name;
        this.descId = descId;
        this.value = value;
    }

    public double getValue() {return this.value;}
    public int getImageId() {return this.imageId;}
    public String getName() {return this.name;}
    public int getDescId() {return this.descId;}

    public void setValue(double newValue) {
        if (newValue >= 0 && newValue <= 1) value = newValue;
    }
}
