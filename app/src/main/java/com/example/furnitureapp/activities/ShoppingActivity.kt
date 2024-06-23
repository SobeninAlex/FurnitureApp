package com.example.furnitureapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.furnitureapp.R
import com.example.furnitureapp.databinding.ActivityShoppingBinding
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }

    private val viewMode by viewModels<CartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.shopping_nav_host_fragment)

        binding.bottomNavigation.setupWithNavController(navController = navController)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewMode.cartProducts.collectLatest {
                    when (it) {
                        is Resource.Administrator -> {}
                        is Resource.Error -> {}
                        is Resource.Initial -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            val count = it.data?.size ?: 0
                            if (count != 0) {
                                binding.bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                                    number = count
                                    backgroundColor = getColor(R.color.g_blue)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}