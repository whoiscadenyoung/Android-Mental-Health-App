package com.csci3397.cadenyoung.groupproject.model;

public class Question {
    private int imageId;
    private int textId;
    private int Answer;

    public  Question(int textId){
        this.textId = textId;
    }
    public Question(int imageId, int textId) {
        this.imageId = imageId;
        this.textId = textId;
        //Answer = answer;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getTextId() {
        return textId;
    }

    public void setTextId(int textId) {
        this.textId = textId;
    }

    public int getAnswer() {
        return Answer;
    }

    public void setAnswer(int answer) {
        Answer = answer;
    }



}

