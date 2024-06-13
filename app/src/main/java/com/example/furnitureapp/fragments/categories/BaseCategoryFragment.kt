package com.example.furnitureapp.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitureapp.adapters.BestProductAdapter
import com.example.furnitureapp.databinding.FragmentBaseCategoryBinding

open class BaseCategoryFragment : Fragment() {

    private var _binding: FragmentBaseCategoryBinding? = null
    private val binding: FragmentBaseCategoryBinding
        get() = _binding ?: throw RuntimeException("FragmentBaseCategoryBinding is null")

    protected val offerProductAdapter by lazy { BestProductAdapter() }
    protected val bestProductsAdapter by lazy { BestProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBaseCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRvOffer()
        setupRvBestProducts()
        scrollChangeListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
    }

    private fun setupRvOffer() {
        binding.rvOfferProducts.apply {
            adapter = offerProductAdapter
        }
    }

}