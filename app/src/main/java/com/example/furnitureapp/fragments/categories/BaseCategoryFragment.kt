package com.example.furnitureapp.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitureapp.R
import com.example.furnitureapp.adapters.BestProductAdapter
import com.example.furnitureapp.databinding.FragmentBaseCategoryBinding
import com.example.furnitureapp.fragments.categories.MainCategoryFragment.Companion.PRODUCT_PARCELABLE_KEY
import com.example.furnitureapp.util.BaseFragment
import com.example.furnitureapp.util.showBottomNavigation

open class BaseCategoryFragment : BaseFragment<FragmentBaseCategoryBinding>() {

    protected val offerProductAdapter by lazy { BestProductAdapter() }
    protected val bestProductsAdapter by lazy { BestProductAdapter() }

    override fun getLayoutId() = R.layout.fragment_base_category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRvOffer()
        setupRvBestProducts()
        scrollChangeListener()
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }

    open fun onOfferProductsPagingRequest() {

    }

    open fun onBestProductPagingRequest() {

    }

    fun showOfferProductsLoading() {
        binding.offerProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideOfferProductsLoading() {
        binding.offerProductsProgressBar.visibility = View.GONE
    }

    fun showBestProductsLoading() {
        binding.bestProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideBestProductsLoading() {
        binding.bestProductsProgressBar.visibility = View.GONE
    }

    private fun scrollChangeListener() {
        binding.rvOfferProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollHorizontally(1) && dx != 0) {
                    onOfferProductsPagingRequest()
                }
            }
        })

        binding.nestedScrollBaseCategory.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (v.getChildAt(0).bottom <= v.height + scrollY) {
                    onBestProductPagingRequest()
                }
            }
        )
    }

    private fun setupRvBestProducts() {
        binding.rvBestProducts.apply {
            adapter = bestProductsAdapter
        }

        bestProductsAdapter.onClick = {
            val bundle = Bundle().apply { putParcelable(PRODUCT_PARCELABLE_KEY, it) }
            findNavController().navigate(R.id.action_homeFragment_to_productsDetailFragment, bundle)
        }
    }

    private fun setupRvOffer() {
        binding.rvOfferProducts.apply {
            adapter = offerProductAdapter
        }

        offerProductAdapter.onClick = {
            val bundle = Bundle().apply { putParcelable(PRODUCT_PARCELABLE_KEY, it) }
            findNavController().navigate(R.id.action_homeFragment_to_productsDetailFragment, bundle)
        }
    }

}