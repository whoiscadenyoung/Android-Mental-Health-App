package com.csci3397.cadenyoung.groupproject.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private String date;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    //TODO add the database reference to be accessed by other fragments

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