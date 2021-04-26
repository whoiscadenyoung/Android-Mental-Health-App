package com.csci3397.cadenyoung.groupproject.model;

public class Coordinate {

    public Coordinate(Double lattitude, Double longitude) {
        this.latitude = lattitude;
        this.longitude = longitude;
    }

    private Double latitude;
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        if (latitude > -90 && latitude< 90)
        {
            this.latitude = latitude;
        }

    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
