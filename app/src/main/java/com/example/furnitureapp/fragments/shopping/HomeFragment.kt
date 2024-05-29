package com.example.furnitureapp.fragments.shopping

import androidx.fragment.app.Fragment
import com.example.furnitureapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding ?: throw RuntimeException("FragmentHomeBinding is null")


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}