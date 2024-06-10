package com.example.furnitureapp.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.furnitureapp.R
import com.example.furnitureapp.adapters.BestDealsAdapter
import com.example.furnitureapp.adapters.BestProductAdapter
import com.example.furnitureapp.adapters.SpecialProductsAdapter
import com.example.furnitureapp.databinding.FragmentMainCategoryBinding
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainCategoryFragment"

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {

    private var _binding: FragmentMainCategoryBinding? = null
    private val binding: FragmentMainCategoryBinding
        get() = _binding ?: throw RuntimeException("FragmentMainCategoryBinding is null")

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductRecyclerView()
        setupBestDealsRecyclerView()
        setupBestProductsRecyclerView()
        viewModelObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupSpecialProductRecyclerView() {
        binding.rvSpecialProducts.apply {
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductAdapter
        }
    }

    private fun setupBestProductsRecyclerView() {
        binding.rvBestProducts.apply {
            adapter = bestProductAdapter
        }
    }

    private fun setupBestDealsRecyclerView() {
        binding.rvBestDealsProducts.apply {
            adapter = bestDealsAdapter
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
                            hideLoading()
                            Log.e(TAG, it.message.toString())
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }

                        is Resource.Initial -> {}

                        is Resource.Loading -> {
                            showLoading()
                        }

                        is Resource.Success -> {
                            bestProductAdapter.differ.submitList(it.data)
                            hideLoading()
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

}