package com.csci3397.cadenyoung.groupproject.model;

public class Stat {
    private int imageId;
    private String name;
    private int descId;
    private int colorId;

    private int progress;

    public Stat(int imageId, String name, int descId, int colorId) {
        this.imageId = imageId;
        this.name = name;
        this.descId = descId;
        this.progress = 0;
        this.colorId = colorId;
    }

    public Stat(int imageId, String name, int descId, int colorId, int progress) {
        this.imageId = imageId;
        this.name = name;
        this.descId = descId;
        this.progress = progress;
        this.colorId = colorId;
    }

    public int getProgress() {return this.progress;}
    public int getImageId() {return this.imageId;}
    public String getName() {return this.name;}
    public int getDescId() {return this.descId;}
    public int getColorId() {return this.colorId;}

    public void setProgress(int newProgress) {
        if (newProgress >= 0 && newProgress <= 100) progress = newProgress;
    }
}
