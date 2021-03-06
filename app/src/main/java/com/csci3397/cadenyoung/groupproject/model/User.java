package com.csci3397.cadenyoung.groupproject.model;


public class User {
    private String email;
    private String name;
    private String dbID;
    private String lastDayTaken;

    private User() {}

    public User(String name, String email, String dbID, String lastDayTaken) {
        this.email = email;
        this.name = name;
        this.dbID = dbID;
        this.lastDayTaken = lastDayTaken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String firstName) {
        this.name = firstName;
    }

    public String getDbID() {
        return dbID;
    }

    public void setDbID(String dbID) {
        this.dbID = dbID;
    }

    public String getLastDayTaken() { return lastDayTaken; }

    public void setLastDayTaken(String lastDayTaken) { this.lastDayTaken = lastDayTaken;}

}