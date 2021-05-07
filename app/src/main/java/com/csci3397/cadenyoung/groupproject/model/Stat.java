package com.csci3397.cadenyoung.groupproject.model;

public class Stat {
    private int imageId;
    private int id;
    private String name;
    private int descId;
    private int colorId;

    private int lastProgress = 50;
    private int progress;

    public Stat(int imageId, int id, String name, int descId, int colorId) {
        this.imageId = imageId;
        this.name = name;
        this.descId = descId;
        this.progress = 0;
        this.colorId = colorId;
        this.id = id;
    }

    public Stat(int imageId, int id, String name, int descId, int colorId, int progress) {
        this.imageId = imageId;
        this.name = name;
        this.descId = descId;
        this.progress = progress;
        this.colorId = colorId;
        this.id = id;
    }

    public Stat() {
        this.imageId = 1;
        this.name = "stat";
        this.descId = 1;
        this.progress = 10;
        this.colorId = 1;
        this.id = 100;
    }

    public int getProgress() {return this.progress;}
    public int getImageId() {return this.imageId;}
    public String getName() {return this.name;}
    public int getDescId() {return this.descId;}
    public int getColorId() {return this.colorId;}

    public void setProgress(int newProgress) {
        if (newProgress >= 0 && newProgress <= 100) {
            lastProgress = progress;
            progress = newProgress;
        }
    }

    public int getLastProgress() {return lastProgress;}

    public void replaceProgress(int newProgress) {
        if (newProgress >= 0 && newProgress <= 100) {
            progress = newProgress;
        }
    }
    public void replaceLastProgress(int newProgress) {
        if (newProgress >= 0 && newProgress <= 100) {
            lastProgress = newProgress;
        }
    }
}
