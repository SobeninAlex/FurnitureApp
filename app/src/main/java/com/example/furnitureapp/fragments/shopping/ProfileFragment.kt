package com.example.furnitureapp.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.furnitureapp.R
import com.example.furnitureapp.databinding.FragmentProfileBinding
import com.example.furnitureapp.util.BaseFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override fun getLayoutId() = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}