package com.example.furnitureapp.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.furnitureapp.adapters.CartProductAdapter
import com.example.furnitureapp.databinding.FragmentCartBinding
import com.example.furnitureapp.firebase.FirebaseCommon
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.util.VerticalItemDecoration
import com.example.furnitureapp.util.formattedPrice
import com.example.furnitureapp.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * activityViewModels<>() -> используется для получения экземпляра ViewModel, связанного с текущей activity.
 * Это означает, что один и тот же экземпляр ViewModel будет использоваться во время всего жизненного цикла activity,
 * включая её создание, отображение и уничтожение. Это полезно, когда данные или состояние должны быть общими для всех
 * фрагментов внутри activity. Однако, если activity уничтожается (например, пользователь переключается на другую activity),
 * то и ViewModel будет уничтожена.
 *
 * viewModels<>() -> используется для получения экземпляра ViewModel, связанного с конкретным фрагментом.
 * Каждый вызов viewModels<>() в рамках одного фрагмента приведет к созданию нового экземпляра ViewModel.
 * Это означает, что каждый фрагмент может иметь свой собственный экземпляр ViewModel, независимый от других
 * фрагментов в той же activity. Это полезно, когда каждый фрагмент требует своего собственного состояния или данных,
 * которые не должны смешиваться с другими фрагментами.
 */

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding: FragmentCartBinding
        get() = _binding ?: throw RuntimeException("FragmentCartBinding is null")

    private val cartAdapter by lazy { CartProductAdapter() }

    private val viewModelCart by activityViewModels<CartViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCartRecyclerView()
        viewModelObservers()
    }

    private fun viewModelObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelCart.cartProducts.collectLatest {
                    when (it) {
                        is Resource.Administrator -> {}

                        is Resource.Error -> {
                            binding.progressBarIndicator.visibility = View.INVISIBLE
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }

                        is Resource.Initial -> {}

                        is Resource.Loading -> {
                            binding.progressBarIndicator.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.progressBarIndicator.visibility = View.INVISIBLE
                            it.data?.let { list ->
                                if (list.isEmpty()) {
                                    showEmptyCart()
                                    hideOtherViews()
                                } else {
                                    hideEmptyCart()
                                    showOtherViews()
                                    cartAdapter.submitList(it.data)
                                }
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelCart.productsPrice.collectLatest { price ->
                    price?.let {
                        binding.tvTotalPrice.text = it.formattedPrice()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelCart.deleteDialog.collectLatest {
                    val alertDialog = AlertDialog.Builder(requireContext()).apply {
                        setTitle("Delete item from cart")
                        setMessage("Do you want to delete this item from your cart?")
                        setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        setPositiveButton("Delete") { dialog, _ ->
                            viewModelCart.deleteCartProduct(it)
                            dialog.dismiss()
                        }
                    }
                    alertDialog.create()
                    alertDialog.show()
                }
            }
        }
    }

    private fun showOtherViews() {
        with(binding) {
            rvCart.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE
        }
    }

    private fun hideOtherViews() {
        with(binding) {
            rvCart.visibility = View.INVISIBLE
            totalBoxContainer.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
        }
    }

    private fun hideEmptyCart() {
        binding.layoutCartEmpty.visibility = View.GONE
    }

    private fun showEmptyCart() {
        binding.layoutCartEmpty.visibility = View.VISIBLE
    }

    private fun setupCartRecyclerView() {
        binding.rvCart.apply {
            adapter = cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }

        cartAdapter.onProductClick = {
            val action = CartFragmentDirections.actionCartFragmentToProductsDetailFragment(it.product)
            findNavController().navigate(action)
        }

        cartAdapter.onPlusClick = {
            viewModelCart.changeQuantity(it, FirebaseCommon.QuantityChanging.INCREASE)
        }

        cartAdapter.onMinusClick = {
            viewModelCart.changeQuantity(it, FirebaseCommon.QuantityChanging.DECREASE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}