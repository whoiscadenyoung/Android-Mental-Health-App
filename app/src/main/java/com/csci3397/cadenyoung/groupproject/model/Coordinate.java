package com.csci3397.cadenyoung.groupproject.model;

public class Coordinate {

    public Coordinate(Double lattitude, Double longitude) {
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    private Double lattitude;
    private Double longitude;

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        if (lattitude > -90 && lattitude< 90)
        {
            this.lattitude = lattitude;
        }

    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
