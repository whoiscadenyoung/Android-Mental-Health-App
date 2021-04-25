package com.csci3397.cadenyoung.groupproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.csci3397.cadenyoung.groupproject.ui.LoginFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*var navController = Navigation.findNavController(this, R.id.fragment)
        //NavigationUI.setupWithNavController(bottomNavigationView, navController)

        var appBarConfiguration = AppBarConfiguration.Builder(
            R.id.loginJavaFragment,
            R.id.registerJavaFragment
        ).build()
        NavigationUI.setupActionBarWithNavController(
            this, navController,
            appBarConfiguration
        )*/

        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,
            LoginFragment()
        )
        fragmentTransaction.commit()

    }
}