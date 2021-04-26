package com.csci3397.cadenyoung.groupproject;

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.csci3397.cadenyoung.groupproject.R
import com.csci3397.cadenyoung.groupproject.ui.LoginFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.fragmentContainer,
            LoginFragment()
        )
        fragmentTransaction.commit()

    }
}