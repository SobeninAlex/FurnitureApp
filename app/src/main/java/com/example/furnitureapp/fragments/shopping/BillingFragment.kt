package com.example.furnitureapp.fragments.shopping

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.furnitureapp.R
import com.example.furnitureapp.adapters.AddressAdapter
import com.example.furnitureapp.adapters.BillingProductAdapter
import com.example.furnitureapp.data.Address
import com.example.furnitureapp.data.CartProduct
import com.example.furnitureapp.databinding.FragmentBillingBinding
import com.example.furnitureapp.util.BaseFragment
import com.example.furnitureapp.util.HorizontalItemDecoration
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.util.formattedPrice
import com.example.furnitureapp.util.setGone
import com.example.furnitureapp.util.setVisible
import com.example.furnitureapp.viewmodel.BillingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BillingFragment : BaseFragment<FragmentBillingBinding>() {

    override fun getLayoutId() = R.layout.fragment_billing

    private val addressAdapter by lazy {
        AddressAdapter(object : AddressAdapter.AddressAdapterListener {
            override fun onAddressClick(address: Address) {

            }
        })
    }

    private val billingProductAdapter by lazy {
        BillingProductAdapter()
    }

    private val viewModelBilling by viewModels<BillingViewModel>()

    private val args by navArgs<BillingFragmentArgs>()

    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        products = args.products.toList()
        totalPrice = args.totalPrice
    }

    override fun onResume() {
        super.onResume()
        viewModelBilling.getUserAddresses()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBillingProductRv()
        setupAddressRv()
        viewModelObserver()
        clickListeners()
        bind()
    }

    private fun bind() = with(binding) {
        tvTotalPrice.text = totalPrice.formattedPrice()
    }

    private fun clickListeners() {
        binding.imageAddAddress.setOnClickListener {
            val action = BillingFragmentDirections.actionBillingFragmentToAddressFragment()
            findNavController().navigate(action)
        }
    }

    private fun viewModelObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelBilling.address.collectLatest {
                    when (it) {
                        is Resource.Administrator -> {}
                        is Resource.Error -> {
                            binding.progressbarAddress.setGone()
                            showToast(it.message.toString())
                        }
                        is Resource.Initial -> {}
                        is Resource.Loading -> {
                            binding.progressbarAddress.setVisible()
                        }
                        is Resource.Success -> {
                            binding.progressbarAddress.setGone()
                            addressAdapter.submitList(it.data)
                        }
                    }
                }
            }
        }
    }

    private fun setupAddressRv() {
        binding.rvAddress.apply {
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupBillingProductRv() {
        binding.rvProducts.apply {
            adapter = billingProductAdapter
            addItemDecoration(HorizontalItemDecoration())
        }

        billingProductAdapter.submitList(products)
    }

}