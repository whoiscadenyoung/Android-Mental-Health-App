package com.csci3397.cadenyoung.groupproject.ui.quiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.csci3397.cadenyoung.groupproject.model.Quiz;

public class QuizViewModel extends ViewModel {

    private MutableLiveData<String> mText;



    private Quiz quiz;

    public QuizViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String text ) {
        mText.setValue(text);
    }

    public Quiz getQuiz() { return quiz; }

    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
}