package com.csci3397.cadenyoung.groupproject.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private String date;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        this.date = "Missing";
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String text ) {
        mText.setValue(text);
    }
    public void setDate(String date)
    {
        this.date = date;
    }
    public String getDate()
    {
        return date;
    }
}