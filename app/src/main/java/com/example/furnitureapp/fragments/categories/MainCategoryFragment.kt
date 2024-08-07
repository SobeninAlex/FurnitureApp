package com.example.furnitureapp.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.furnitureapp.R
import com.example.furnitureapp.adapters.BestDealsAdapter
import com.example.furnitureapp.adapters.BestProductAdapter
import com.example.furnitureapp.adapters.SpecialProductsAdapter
import com.example.furnitureapp.databinding.FragmentMainCategoryBinding
import com.example.furnitureapp.util.BaseFragment
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.util.showBottomNavigation
import com.example.furnitureapp.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainCategoryFragment"

@AndroidEntryPoint
class MainCategoryFragment : BaseFragment<FragmentMainCategoryBinding>() {

    private val specialProductAdapter by lazy {
        SpecialProductsAdapter()
    }

    private val bestProductAdapter by lazy {
        BestProductAdapter()
    }

    private val bestDealsAdapter by lazy {
        BestDealsAdapter()
    }

    private val viewModelMainCategory by viewModels<MainCategoryViewModel>()

    override fun getLayoutId() = R.layout.fragment_main_category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductRecyclerView()
        setupBestDealsRecyclerView()
        setupBestProductsRecyclerView()
        viewModelObserver()
        scrollChangeListener()
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }

    private fun scrollChangeListener() {
        binding.nestedScrollMainCategory.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (v.getChildAt(0).bottom <= v.height + scrollY) {
                    viewModelMainCategory.fetchBestProducts()
                }
            }
        )
    }

    private fun setupSpecialProductRecyclerView() {
        binding.rvSpecialProducts.apply {
            adapter = specialProductAdapter
        }

        specialProductAdapter.onClick = {
            val bundle = Bundle().apply { putParcelable(PRODUCT_PARCELABLE_KEY, it) }
            findNavController().navigate(R.id.action_homeFragment_to_productsDetailFragment, bundle)
        }
    }

    private fun setupBestProductsRecyclerView() {
        binding.rvBestProducts.apply {
            adapter = bestProductAdapter
        }

        bestProductAdapter.onClick = {
            val bundle = Bundle().apply { putParcelable(PRODUCT_PARCELABLE_KEY, it) }
            findNavController().navigate(R.id.action_homeFragment_to_productsDetailFragment, bundle)
        }
    }

    private fun setupBestDealsRecyclerView() {
        binding.rvBestDealsProducts.apply {
            adapter = bestDealsAdapter
        }

        bestDealsAdapter.onClick = {
            val bundle = Bundle().apply { putParcelable(PRODUCT_PARCELABLE_KEY, it) }
            findNavController().navigate(R.id.action_homeFragment_to_productsDetailFragment, bundle)
        }
    }

    private fun viewModelObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelMainCategory.specialProducts.collectLatest {
                    when (it) {
                        is Resource.Administrator -> {}

                        is Resource.Error -> {
                            hideLoading()
                            Log.e(TAG, it.message.toString())
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }

                        is Resource.Initial -> {}

                        is Resource.Loading -> {
                            showLoading()
                        }

                        is Resource.Success -> {
                            specialProductAdapter.differ.submitList(it.data)
                            hideLoading()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelMainCategory.bestDealsProducts.collectLatest {
                    when (it) {
                        is Resource.Administrator -> {}

                        is Resource.Error -> {
                            hideLoading()
                            Log.e(TAG, it.message.toString())
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }

                        is Resource.Initial -> {}

                        is Resource.Loading -> {
                            showLoading()
                        }

                        is Resource.Success -> {
                            bestDealsAdapter.differ.submitList(it.data)
                            hideLoading()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelMainCategory.bestProducts.collectLatest {
                    when (it) {
                        is Resource.Administrator -> {}

                        is Resource.Error -> {
                            binding.bestProductsProgressBar.visibility = View.GONE
                            Log.e(TAG, it.message.toString())
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }

                        is Resource.Initial -> {}

                        is Resource.Loading -> {
                            binding.bestProductsProgressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            bestProductAdapter.differ.submitList(it.data)
                            binding.bestProductsProgressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun hideLoading() {
        binding.mainCategoryProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCategoryProgressBar.visibility = View.VISIBLE
    }

    companion object {
        const val PRODUCT_PARCELABLE_KEY = "product"
    }

}