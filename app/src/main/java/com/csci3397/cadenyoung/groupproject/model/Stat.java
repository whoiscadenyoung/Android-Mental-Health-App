package com.csci3397.cadenyoung.groupproject.model;

public class Stat {
    private int imageId;
    private String name;
    private int descId;
    private int colorId;

    private int value;

    public Stat(int imageId, String name, int descId, int colorId) {
        this.imageId = imageId;
        this.name = name;
        this.descId = descId;
        this.value = 0;
        this.colorId = colorId;
    }

    public Stat(int imageId, String name, int descId, int colorId, int value) {
        this.imageId = imageId;
        this.name = name;
        this.descId = descId;
        this.value = value;
        this.colorId = colorId;
    }

    public double getValue() {return this.value;}
    public int getImageId() {return this.imageId;}
    public String getName() {return this.name;}
    public int getDescId() {return this.descId;}
    public int getColorId() {return this.colorId;}

    public void setValue(int newValue) {
        if (newValue >= 0 && newValue <= 100) value = newValue;
    }
}
