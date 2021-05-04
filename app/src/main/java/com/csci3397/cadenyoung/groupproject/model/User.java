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

//    public User(User user) {
//        this.email = user.email;
//        this.name = user.name;
//        this.dbID = user.dbID;
//        this.lastDayTaken = user.lastDayTaken;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return name;
    }

    public void setFirstName(String firstName) {
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