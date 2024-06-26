package com.example.furnitureapp.fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.furnitureapp.R
import com.example.furnitureapp.data.User
import com.example.furnitureapp.databinding.FragmentUserAccountBinding
import com.example.furnitureapp.dialog.setupBottomSheetDialog
import com.example.furnitureapp.util.BaseFragment
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.util.setGone
import com.example.furnitureapp.util.setInvisible
import com.example.furnitureapp.util.setVisible
import com.example.furnitureapp.viewmodel.UserAccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserAccountFragment : BaseFragment<FragmentUserAccountBinding>() {

    override fun getLayoutId() = R.layout.fragment_user_account
    private val viewModelUserAccount by viewModels<UserAccountViewModel>()
    private var imageUri: Uri? = null
    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageActivityResultLauncher = registerForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            imageUri = it.data?.data
            Glide.with(this).load(imageUri).into(binding.imageUser)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelObserver()
        clickListeners()
    }

    private fun clickListeners() {
        binding.buttonSave.setOnClickListener {
            with(binding) {
                val firstName = edFirstName.text.toString().trim()
                val lastName = edLastName.text.toString().trim()
                val email = edEmail.text.toString().trim()

                val user = User(firstName, lastName, email)
                viewModelUserAccount.updateUser(user, imageUri)
            }
        }

        binding.imageEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imageActivityResultLauncher.launch(intent)
        }

        binding.tvUpdatePassword.setOnClickListener {
            setupBottomSheetDialog {

            }
        }
    }

    private fun viewModelObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelUserAccount.user.collectLatest {
                    when (it) {
                        is Resource.Administrator -> {}
                        is Resource.Error -> {
                            binding.progressbarAccount.setGone()
                            showToast(it.message.toString())
                        }
                        is Resource.Initial -> {}
                        is Resource.Loading -> {
                            showUserLoading()
                        }
                        is Resource.Success -> {
                            hideUserLoading()
                            showUserInfo(user = it.data!!)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelUserAccount.updateInfo.collectLatest {
                    when (it) {
                        is Resource.Administrator -> {}
                        is Resource.Error -> {
                            binding.buttonSave.revertAnimation()
                            showToast(it.message.toString())
                        }
                        is Resource.Initial -> {}
                        is Resource.Loading -> {
                            binding.buttonSave.startAnimation()
                        }
                        is Resource.Success -> {
                            binding.buttonSave.revertAnimation()
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }
    }

    private fun showUserInfo(user: User) {
        with(binding) {
            Glide.with(this@UserAccountFragment)
                .load(user.imagePath)
                .error(R.drawable.ic_account)
                .into(imageUser)
            edFirstName.setText(user.firstName)
            edLastName.setText(user.lastName)
            edEmail.setText(user.email)
        }
    }

    private fun  hideUserLoading() {
        with(binding) {
            progressbarAccount.setGone()
            infoUserContainer.setVisible()
        }
    }

    private fun showUserLoading() {
        with(binding) {
            progressbarAccount.setVisible()
            infoUserContainer.setInvisible()
        }
    }
}