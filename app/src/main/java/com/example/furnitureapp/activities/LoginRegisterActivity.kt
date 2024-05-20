package com.example.furnitureapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.furnitureapp.R
import com.example.furnitureapp.fragments.loginRegister.LoginFragment
import com.example.furnitureapp.fragments.loginRegister.RegisterFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
    }

}