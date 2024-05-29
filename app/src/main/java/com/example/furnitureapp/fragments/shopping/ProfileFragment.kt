package com.example.furnitureapp.fragments.shopping

import androidx.fragment.app.Fragment
import com.example.furnitureapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding is null")


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}