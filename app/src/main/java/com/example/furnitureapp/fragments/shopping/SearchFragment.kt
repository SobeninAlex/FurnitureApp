package com.example.furnitureapp.fragments.shopping

import androidx.fragment.app.Fragment
import com.example.furnitureapp.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchBinding is null")


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}