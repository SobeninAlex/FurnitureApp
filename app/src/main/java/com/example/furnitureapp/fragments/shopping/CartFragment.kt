package com.example.furnitureapp.fragments.shopping

import androidx.fragment.app.Fragment
import com.example.furnitureapp.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding: FragmentCartBinding
        get() = _binding ?: throw RuntimeException("FragmentCartBinding is null")


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}