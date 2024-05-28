package com.example.furnitureapp.fragments.loginRegister

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.furnitureapp.R
import com.example.furnitureapp.activities.ShoppingActivity
import com.example.furnitureapp.databinding.FragmentLoginBinding
import com.example.furnitureapp.dialog.setupBottomSheetDialog
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "LoginFragment"

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding ?: throw RuntimeException("FragmentLoginBinding is null")

    private val viewModelLogin by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.apply {
            buttonLogin.setOnClickListener {
                val email = edEmail.text.toString().trim()
                val password = edPassword.text.toString()
                viewModelLogin.login(email, password)
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            setupBottomSheetDialog { email ->
                viewModelLogin.resetPassword(email)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelLogin.resetPassword.collect {
                    when (it) {
                        is Resource.Error -> {
                            Snackbar.make(
                                requireView(),
                                "Error: ${it.message}",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }

                        is Resource.Initial -> {}

                        is Resource.Loading -> {}

                        is Resource.Success -> {
                            Snackbar.make(
                                requireView(),
                                getString(R.string.reset_link_was_send_to_your_email),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelLogin.login.collect {
                    when (it) {
                        is Resource.Error -> {
                            binding.buttonLogin.revertAnimation()
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }

                        is Resource.Initial -> {}

                        is Resource.Loading -> {
                            binding.buttonLogin.startAnimation()
                        }

                        is Resource.Success -> {
                            binding.buttonLogin.revertAnimation()

                            Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}