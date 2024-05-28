package com.example.furnitureapp.fragments.loginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.furnitureapp.R
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.furnitureapp.databinding.FragmentAccountOptionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountOptionsFragment : Fragment() {

    private var _binding: FragmentAccountOptionsBinding? = null
    private val binding: FragmentAccountOptionsBinding
        get() = _binding ?: throw RuntimeException("FragmentAccountOptionsBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLoginAccountOptions.setOnClickListener {
            findNavController().navigate(R.id.action_accountOptionsFragment_to_loginFragment)
        }

        binding.buttonRegisterAccountOptions.setOnClickListener {
            findNavController().navigate(R.id.action_accountOptionsFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}