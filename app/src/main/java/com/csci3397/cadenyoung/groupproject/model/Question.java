package com.csci3397.cadenyoung.groupproject.model;

public class Question {
    private int imageId;
    private int textId;
    private int answer;
    private String questionType;

    public Question(int textId, String type){
        this.textId = textId;
        this.questionType = type;
    }
    public Question(int imageId, int textId) {
        this.imageId = imageId;
        this.textId = textId;
        this.answer = -1;
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
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getQuestionType() {return this.questionType;}



}

