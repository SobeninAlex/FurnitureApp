package com.example.furnitureapp.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.furnitureapp.R
import com.example.furnitureapp.activities.ShoppingActivity
import com.example.furnitureapp.databinding.FragmentIntroductionBinding
import com.example.furnitureapp.viewmodel.IntroductionViewModel
import com.example.furnitureapp.viewmodel.IntroductionViewModel.Companion.ACCOUNT_OPTIONS_FRAGMENT
import com.example.furnitureapp.viewmodel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroductionFragment : Fragment() {

    private var _binding: FragmentIntroductionBinding? = null
    private val binding: FragmentIntroductionBinding
        get() = _binding ?: throw RuntimeException("FragmentIntroductionBinding is null")

    private val viewModelIntroduction by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroductionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModelIntroduction.navigate.collect {
                when (it) {
                    SHOPPING_ACTIVITY -> {
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    ACCOUNT_OPTIONS_FRAGMENT -> {
                        findNavController().navigate(it)
                    }

                    else -> Unit
                }
            }
        }

        binding.buttonStart.setOnClickListener {
            viewModelIntroduction.startButtonClick()
            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}