package com.example.furnitureapp.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.furnitureapp.R
import com.example.furnitureapp.adapters.AllOrdersAdapter
import com.example.furnitureapp.data.order.Order
import com.example.furnitureapp.databinding.FragmentOrdersBinding
import com.example.furnitureapp.util.BaseFragment
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.util.setGone
import com.example.furnitureapp.util.setVisible
import com.example.furnitureapp.viewmodel.AllOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AllOrdersFragment : BaseFragment<FragmentOrdersBinding>() {

    private val viewModelOrders by viewModels<AllOrdersViewModel>()

    override fun getLayoutId() = R.layout.fragment_orders
    private val allOrdersAdapter by lazy {
        AllOrdersAdapter(object : AllOrdersAdapter.OrdersAdapterListener {
            override fun onOrderClick(order: Order) {

            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOrdersRv()
        viewModelObserver()
    }

    private fun viewModelObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelOrders.allOrders.collectLatest {
                    when (it) {
                        is Resource.Administrator -> {}
                        is Resource.Error -> {
                            binding.progressbarAllOrders.setGone()
                            showSnackbar(it.message.toString())
                        }
                        is Resource.Initial -> {}
                        is Resource.Loading -> {
                            binding.progressbarAllOrders.setVisible()
                        }
                        is Resource.Success -> {
                            binding.progressbarAllOrders.setGone()
                            allOrdersAdapter.submitList(it.data)
                            if (it.data.isNullOrEmpty()) {
                                binding.tvEmptyOrders.setVisible()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupOrdersRv() {
        binding.rvAllOrders.apply {
            adapter = allOrdersAdapter
        }
    }

}