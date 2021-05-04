package com.csci3397.cadenyoung.groupproject.database;

import com.google.android.gms.maps.model.LatLng;

public class LocationHelperClass {
    //String userID;
    LatLng lastLocation;

    public LocationHelperClass(LatLng lastLocation) {
        //this.userID = userID;
        this.lastLocation = lastLocation;
    }

    /*public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }*/

    public LatLng getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(LatLng lastLocation) {
        this.lastLocation = lastLocation;
    }

}
