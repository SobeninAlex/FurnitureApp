package com.example.furnitureapp.fragments.shopping

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
import androidx.navigation.fragment.navArgs
import com.example.furnitureapp.R
import com.example.furnitureapp.activities.ShoppingActivity
import com.example.furnitureapp.adapters.ColorsAdapter
import com.example.furnitureapp.adapters.ImagesViewpager
import com.example.furnitureapp.adapters.SizesAdapter
import com.example.furnitureapp.data.CartProduct
import com.example.furnitureapp.data.Product
import com.example.furnitureapp.databinding.FragmentProductDetailsBinding
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.util.hideBottomNavigation
import com.example.furnitureapp.viewmodel.DetailsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductsDetailFragment : Fragment() {

    private val args by navArgs<ProductsDetailFragmentArgs>()

    private val viewModelDetails by viewModels<DetailsViewModel>()

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding: FragmentProductDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentProductDetailsBinding is null")

    private val viewpagerImages by lazy { ImagesViewpager() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private val sizesAdapter by lazy { SizesAdapter() }

    private var selectedColor: Int? = null
    private var selectedSize: String? = null

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

        clickListeners(product)

        viewModelObserver()
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

    private fun clickListeners(product: Product) {
        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonAddToCart.setOnClickListener {
            val cartProduct = CartProduct(
                product = product,
                selectedColor = selectedColor,
                selectedSize = selectedSize
            )
            viewModelDetails.addUpdateProductInCart(cartProduct)
        }
    }

    private fun viewModelObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelDetails.addToCart.collectLatest {
                    when (it) {
                        is Resource.Administrator -> {}
                        is Resource.Error -> {
                            binding.buttonAddToCart.revertAnimation()
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Initial -> {}
                        is Resource.Loading -> {
                            binding.buttonAddToCart.startAnimation()
                        }
                        is Resource.Success -> {
                            binding.buttonAddToCart.revertAnimation()
                            Toast.makeText(requireContext(), "Product ${it.data?.product?.name} is added", Toast.LENGTH_SHORT).show()
                            binding.buttonAddToCart.setBackgroundColor(resources.getColor(R.color.black))
                        }
                    }
                }
            }
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

        colorsAdapter.onItemClick = {
            selectedColor = it
        }
    }

    private fun setupSizesRv(product: Product) {
        binding.rvSizes.apply {
            adapter = sizesAdapter
        }
        product.sizes?.let { sizesAdapter.submitList(it) }

        sizesAdapter.onItemClick = {
            selectedSize = it
        }
    }

}