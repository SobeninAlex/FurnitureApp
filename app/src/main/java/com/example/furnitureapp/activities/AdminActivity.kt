package com.example.furnitureapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.furnitureapp.R
import com.example.furnitureapp.databinding.ActivityAdminBinding
import com.example.furnitureapp.viewmodel.AdminViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAdminBinding.inflate(layoutInflater)
    }

    private val viewModelAdmin by viewModels<AdminViewModel>()

    private var selectedImages = mutableListOf<Uri>()

    private val selectImagesActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data

                //Multiple images selected
                if (intent?.clipData != null) {
                    val count = intent.clipData?.itemCount ?: 0
                    (0 until count).forEach {
                        val imageUri = intent.clipData?.getItemAt(it)?.uri
                        imageUri?.let { uri ->
                            selectedImages.add(uri)
                        }
                    }
                } else {
                    val imageUri = intent?.data
                    imageUri?.let { selectedImages.add(it) }
                }

                updateImages()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initSpinner()
        clickListeners()
        viewModelObserver()
    }

    private fun initSpinner() {
        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.categories)
        )

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.spinnerCategory.adapter = arrayAdapter
    }

    private fun saveProduct() {
        with(binding) {
            val name = edName.text.toString().trim()
            val category = spinnerCategory.selectedItem.toString()
            val price = edPrice.text.toString().trim()
            val offerPercentage = edOfferPercentage.text.toString().trim()
            val description = edDescription.text.toString().trim()
            val sizes = edSizes.text.toString().trim()

            viewModelAdmin.saveInStorage(
                selectedImages = selectedImages,
                context = this@AdminActivity,
                name = name,
                category = category,
                price = price,
                offerPercentage = offerPercentage,
                description = description,
                size = sizes
            )
        }
    }

    private fun validateInformation(): Boolean {
        if (binding.edPrice.text.toString().trim().isEmpty()) {
            return false
        }

        if (binding.edName.text.toString().trim().isEmpty()) {
            return false
        }

//        if (binding.edCategory.text.toString().trim().isEmpty()) {
//            return false
//        }

        if (selectedImages.isEmpty()) {
            return false
        }

        return true
    }

    private fun updateImages() {
        binding.tvSelectedImages.text = selectedImages.size.toString()
    }

    private fun viewModelObserver() {
        viewModelAdmin.selectedColors.observe(this) {
            var colors = ""
            it.forEach {
                colors = "$colors ${Integer.toHexString(it)}"
            }
            binding.tvSelectedColors.text = colors
        }

        viewModelAdmin.loadingStatus.observe(this) { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun clickListeners() {
        binding.buttonColorPicker.setOnClickListener {
            viewModelAdmin.createColorPickerDialog(this)
        }

        binding.buttonImagesPicker.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type = "image/*"
            selectImagesActivityResult.launch(intent)
        }

        binding.btnSaveProduct.setOnClickListener {
            val productValidation = validateInformation()

            if (!productValidation) {
                Toast.makeText(this, getString(R.string.check_your_inputs), Toast.LENGTH_SHORT).show()
            }

            saveProduct()
        }
    }

}