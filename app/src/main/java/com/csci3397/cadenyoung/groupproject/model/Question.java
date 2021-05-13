package com.csci3397.cadenyoung.groupproject.model;

public class Question {
    private int textId;
    private int answer;
    private String questionType;

    public Question(int textId, String type){
        this.textId = textId;
        this.questionType = type;
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

