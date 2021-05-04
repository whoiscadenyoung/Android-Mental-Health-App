package com.csci3397.cadenyoung.groupproject;

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.csci3397.cadenyoung.groupproject.ui.signin.LoginFragment



class MainActivity : AppCompatActivity() {
    lateinit var INSTANCE: MainActivity

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
        INSTANCE = this

    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        var isAvailable = false

        if (networkCapabilities != null) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                isAvailable = true
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                isAvailable = true
            }
        } else {
            Toast.makeText(this, "Sorry, network is not available", Toast.LENGTH_LONG)
                .show()
        }
        return isAvailable
    }

    fun getActivityInstance(): MainActivity {
        return INSTANCE
    }



}