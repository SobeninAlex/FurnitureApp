package com.example.furnitureapp.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.furnitureapp.R
import com.example.furnitureapp.activities.ShoppingActivity
import com.example.furnitureapp.adapters.ColorsAdapter
import com.example.furnitureapp.adapters.ImagesViewpager
import com.example.furnitureapp.adapters.SizesAdapter
import com.example.furnitureapp.data.Product
import com.example.furnitureapp.databinding.FragmentProductDetailsBinding
import com.example.furnitureapp.util.hideBottomNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductsDetailFragment : Fragment() {

    private val args by navArgs<ProductsDetailFragmentArgs>()

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding: FragmentProductDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentProductDetailsBinding is null")

    private val viewpagerImages by lazy { ImagesViewpager() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private val sizesAdapter by lazy { SizesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNavigation()
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizesRv(product)
        setupColorsRv(product)
        setupViewpager(product)

        initViews(product)

        clickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews(product: Product) {
        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDescription.text = product.description

            if (product.colors.isNullOrEmpty()) {
                tvProductColors.visibility = View.INVISIBLE
            }

            if (product.sizes.isNullOrEmpty()) {
                tvProductSizes.visibility = View.INVISIBLE
            }
        }
    }

    private fun clickListeners() {
        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupViewpager(product: Product) {
        binding.apply {
            viewPagerProductImg.adapter = viewpagerImages
        }
        viewpagerImages.differ.submitList(product.images)
    }

    private fun setupColorsRv(product: Product) {
        binding.rvColors.apply {
            adapter = colorsAdapter
        }
        product.colors?.let { colorsAdapter.differ.submitList(it) }
    }

    private fun setupSizesRv(product: Product) {
        binding.rvSizes.apply {
            adapter = sizesAdapter
        }
        product.sizes?.let { sizesAdapter.submitList(it) }
    }

}