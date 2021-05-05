package com.csci3397.cadenyoung.groupproject.model;

import com.google.android.gms.maps.model.LatLng;

public class Location {

    private Double latitude;
    private Double longitude;

    public Location() {}

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


//    public Location(LatLng lastLocation) {
//        this.lastLocation = lastLocation;
//    }
//
//    public LatLng getLastLocation() {
//        return lastLocation;
//    }
//
//    public void setLastLocation(LatLng lastLocation) {
//        this.lastLocation = lastLocation;
//    }

}
