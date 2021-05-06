package com.csci3397.cadenyoung.groupproject.model;

import java.util.ArrayList;

public class UserStats {
    private String userID;
    private int stat1progress;
    private int stat2progress;
    private int stat3progress;
    private int stat4progress;
    private int stat5progress;
    private int stat6progress;
    private int stat7progress;

    private UserStats() {}

    public UserStats(String userID, int stat1progress, int stat2progress, int stat3progress, int stat4progress, int stat5progress, int stat6progress, int stat7progress) {
        this.userID = userID;
        this.stat1progress = stat1progress;
        this.stat2progress = stat2progress;
        this.stat3progress = stat3progress;
        this.stat4progress = stat4progress;
        this.stat5progress = stat5progress;
        this.stat6progress = stat6progress;
        this.stat7progress = stat7progress;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getStat1progress() {
        return stat1progress;
    }

    public void setStat1progress(int stat1progress) {
        this.stat1progress = stat1progress;
    }

    public int getStat2progress() {
        return stat2progress;
    }

    public void setStat2progress(int stat2progress) {
        this.stat2progress = stat2progress;
    }

    public int getStat3progress() {
        return stat3progress;
    }

    public void setStat3progress(int stat3progress) {
        this.stat3progress = stat3progress;
    }

    public int getStat4progress() {
        return stat4progress;
    }

    public void setStat4progress(int stat4progress) {
        this.stat4progress = stat4progress;
    }

    public int getStat5progress() {
        return stat5progress;
    }

    public void setStat5progress(int stat5progress) {
        this.stat5progress = stat5progress;
    }

    public int getStat6progress() {
        return stat6progress;
    }

    public void setStat6progress(int stat6progress) {
        this.stat6progress = stat6progress;
    }

    public int getStat7progress() {
        return stat7progress;
    }

    public void setStat7progress(int stat7progress) {
        this.stat7progress = stat7progress;
    }

}
