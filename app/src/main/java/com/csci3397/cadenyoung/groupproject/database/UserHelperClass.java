package com.csci3397.cadenyoung.groupproject.database;

public class UserHelperClass {
    String name;
    String contact;
    String userID;

    public UserHelperClass(String name, String email, String userID) {
        this.name = name;
        this.contact = email;
        this.userID = userID;
    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return contact;
    }

    public void setEmail(String contact) {
        this.contact = contact;
    }

}
