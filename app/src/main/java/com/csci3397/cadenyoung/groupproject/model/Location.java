package com.csci3397.cadenyoung.groupproject.model;

import com.google.android.gms.maps.model.LatLng;

public class Location {
    LatLng lastLocation;

    public Location(LatLng lastLocation) {
        this.lastLocation = lastLocation;
    }

    public LatLng getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(LatLng lastLocation) {
        this.lastLocation = lastLocation;
    }

}
