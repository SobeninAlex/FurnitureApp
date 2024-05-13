package com.example.furnitureapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.furnitureapp.R
import com.example.furnitureapp.fragments.loginRegister.LoginFragment
import com.example.furnitureapp.fragments.loginRegister.RegisterFragment

class LoginRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        //TODO: test
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, LoginFragment())
            .commit()
    }
}