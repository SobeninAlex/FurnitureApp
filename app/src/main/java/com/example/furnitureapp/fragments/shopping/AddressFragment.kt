package com.example.furnitureapp.fragments.shopping

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.furnitureapp.R
import com.example.furnitureapp.data.Address
import com.example.furnitureapp.databinding.FragmentAddressBinding
import com.example.furnitureapp.util.BaseFragment
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.util.setGone
import com.example.furnitureapp.util.setVisible
import com.example.furnitureapp.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddressFragment : BaseFragment<FragmentAddressBinding>() {

    private val viewModelAddress by viewModels<AddressViewModel>()

    private val args by navArgs<AddressFragmentArgs>()

    override fun getLayoutId() = R.layout.fragment_address

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val address = args.address

        if (address == null) {
            binding.btnDelete.setGone()
        } else {
            with(binding) {
                edAddressTitle.setText(address.addressTitle)
                edFullName.setText(address.fullName)
                edState.setText(address.state)
                edPhone.setText(address.phone)
                edCity.setText(address.city)
                edStreet.setText(address.street)
            }
        }

        clickListeners()
        viewModelObserver()
    }

    private fun viewModelObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelAddress.addNewAddress.collectLatest {
                    when (it) {
                        is Resource.Administrator -> {}
                        is Resource.Error -> {
                            binding.progressBarAddress.setGone()
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Initial -> {}
                        is Resource.Loading -> {
                            binding.progressBarAddress.setVisible()
                        }
                        is Resource.Success -> {
                            binding.progressBarAddress.setGone()
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelAddress.error.collectLatest {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clickListeners() {
        binding.btnSave.setOnClickListener {
            with(binding) {
                val addressTitle = edAddressTitle.text.toString()
                val city = edCity.text.toString()
                val phone = edPhone.text.toString()
                val state = edState.text.toString()
                val street = edStreet.text.toString()
                val fullName = edFullName.text.toString()

                val address = Address(
                    addressTitle = addressTitle,
                    fullName = fullName,
                    street = street,
                    phone = phone,
                    city = city,
                    state = state
                )

                viewModelAddress.addAddress(address)
            }
        }
    }

}