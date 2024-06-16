package com.example.furnitureapp.util

import android.view.View
import androidx.fragment.app.Fragment
import com.example.furnitureapp.R
import com.example.furnitureapp.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigation() {
    val bottomNavigationBar = (activity as ShoppingActivity)
        .findViewById<BottomNavigationView>(R.id.bottom_navigation)
    bottomNavigationBar.visibility = View.GONE
}

fun Fragment.showBottomNavigation() {
    val bottomNavigationBar = (activity as ShoppingActivity)
        .findViewById<BottomNavigationView>(R.id.bottom_navigation)
    bottomNavigationBar.visibility = View.VISIBLE
}