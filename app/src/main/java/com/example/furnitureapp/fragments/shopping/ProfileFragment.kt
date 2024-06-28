package com.example.furnitureapp.fragments.shopping

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.furnitureapp.R
import com.example.furnitureapp.activities.LoginRegisterActivity
import com.example.furnitureapp.data.User
import com.example.furnitureapp.databinding.FragmentProfileBinding
import com.example.furnitureapp.util.BaseFragment
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.util.setGone
import com.example.furnitureapp.util.setVisible
import com.example.furnitureapp.util.showBottomNavigation
import com.example.furnitureapp.viewmodel.ProfileViewModel
import com.shuhart.stepview.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModelProfile by viewModels<ProfileViewModel>()

    override fun getLayoutId() = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelObserver()
        bind()
        clickListeners()
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }

    private fun bind() = with(binding) {
        tvVersion.text = "Version ${BuildConfig.VERSION_CODE}"
    }

    private fun clickListeners() = with(binding) {
        constraintProfile.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToUserAccountFragment()
            findNavController().navigate(action)
        }

        linearAllOrders.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToOrdersFragment()
            findNavController().navigate(action)
        }

        linearBilling.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToBillingFragment(
                totalPrice = 0f,
                products = emptyArray(),
                payment = false
            )
            findNavController().navigate(action)
        }

        linearLogOut.setOnClickListener {
            viewModelProfile.logout()
            Intent(requireActivity(), LoginRegisterActivity::class.java).also {
                startActivity(it)
            }
            requireActivity().finish()
        }
    }

    private fun viewModelObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelProfile.user.collectLatest {
                    when (it) {
                        is Resource.Administrator -> {}
                        is Resource.Error -> {
                            binding.progressbarSettings.setGone()
                            showToast(it.message.toString())
                        }

                        is Resource.Initial -> {}
                        is Resource.Loading -> {
                            binding.progressbarSettings.setVisible()
                        }

                        is Resource.Success -> {
                            binding.progressbarSettings.setGone()
                            showUser(it.data!!)
                        }
                    }
                }
            }
        }
    }

    private fun showUser(user: User) = with(binding) {
        Glide.with(requireView())
            .load(user.imagePath)
            .placeholder(R.drawable.ic_account)
            .error(R.drawable.ic_account)
            .into(imageUser)
        tvUserName.text = "${user.firstName} ${user.lastName}"
    }

}