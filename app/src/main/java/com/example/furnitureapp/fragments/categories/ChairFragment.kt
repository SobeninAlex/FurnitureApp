package com.example.furnitureapp.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.furnitureapp.data.Category
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.viewmodel.CategoryViewModel
import com.example.furnitureapp.viewmodel.factory.BaseCategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ChairFragment : BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    private val viewModelCategory by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(firestore, Category.Chair)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelObserve()
    }

    private fun viewModelObserve() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelCategory.offerProducts.collectLatest {
                    when (it) {
                        is Resource.Administrator -> {}
                        is Resource.Error -> {
                            Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                            hideOfferProductsLoading()
                        }
                        is Resource.Initial -> {}
                        is Resource.Loading -> {
                            showOfferProductsLoading()
                        }
                        is Resource.Success -> {
                            offerProductAdapter.differ.submitList(it.data)
                            hideOfferProductsLoading()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelCategory.bestProducts.collectLatest {
                    when (it) {
                        is Resource.Administrator -> {}
                        is Resource.Error -> {
                            Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                            hideBestProductsLoading()
                        }
                        is Resource.Initial -> {}
                        is Resource.Loading -> {
                            showBestProductsLoading()
                        }
                        is Resource.Success -> {
                            bestProductsAdapter.differ.submitList(it.data)
                            hideBestProductsLoading()
                        }
                    }
                }
            }
        }
    }

    override fun onOfferProductsPagingRequest() {

    }

    override fun onBestProductPagingRequest() {

    }

}