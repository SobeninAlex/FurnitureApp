package com.example.furnitureapp.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furnitureapp.R
import com.example.furnitureapp.data.Product
import com.example.furnitureapp.util.Constants.PRODUCTS_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class AdminViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: StorageReference
) : ViewModel() {

    private val _savedMessage = MutableLiveData<String>()
    val savedMessage: LiveData<String> = _savedMessage

    private val colorsList = mutableListOf<Int>()
    private val _selectedColors = MutableLiveData<List<Int>>()
    val selectedColors: LiveData<List<Int>> get() = _selectedColors

    private val _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean> = _loadingStatus

    fun createColorPickerDialog(context: Context) {
        ColorPickerDialog.Builder(context)
            .setTitle(context.getString(R.string.product_color_title_dialog))
            .setPositiveButton(context.getString(R.string.select_btn_dialog), object :
                ColorEnvelopeListener {
                override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                    envelope?.let {
                        colorsList.add(it.color)
                        _selectedColors.value = colorsList
                    }
                }
            })
            .setNegativeButton(context.getString(R.string.cancel_btn)) { colorPicker, _->
                colorPicker.dismiss()
            }
            .show()
    }

    private fun getSizesList(sizesStr: String): List<String>? {
        if (sizesStr.isEmpty()) {
            return null
        }
        return sizesStr.split(",")
    }

    private fun getImagesByteArrays(selectedImages: List<Uri>, context: Context): List<ByteArray> {
        val imagesByteArray = mutableListOf<ByteArray>()
        selectedImages.forEach {
            val stream = ByteArrayOutputStream()
            val imageBmp = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            if (imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                imagesByteArray.add(stream.toByteArray())
            }
        }
        return imagesByteArray
    }

    fun saveInStorage(
        selectedImages: List<Uri>,
        context: Context,
        name: String,
        category: String,
        price: String,
        offerPercentage: String,
        description: String,
        size: String
    ) {
        val sizes = getSizesList(size)
        val imagesByteArrays = getImagesByteArrays(selectedImages, context)
        val images = mutableListOf<String>()

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _loadingStatus.value = true
            }

            try {
                async {
                    imagesByteArrays.forEach {
                        val id = UUID.randomUUID().toString()
                        launch {
                            val imageStore = storage.child("product/images/$id")
                            val result = imageStore.putBytes(it).await()
                            val downloadUrl = result.storage.downloadUrl.await().toString()
                            images.add(downloadUrl)
                        }
                    }
                }.await()
            } catch (ex: Exception) {
                ex.printStackTrace()
                withContext(Dispatchers.Main) {
                    _loadingStatus.value = false
                }
            }

            val product = Product(
                id = UUID.randomUUID().toString(),
                name = name,
                category = category,
                price = price.toFloat(),
                offerPercentage = if (offerPercentage.isEmpty()) null else offerPercentage.toFloat(),
                description = description.ifEmpty { null },
                colors = if(selectedColors.value?.isEmpty() == true) null else selectedColors.value,
                sizes = sizes,
                images = images
            )

            firestore.collection(PRODUCTS_COLLECTION)
                .document(product.id)
                .set(product)
                .addOnSuccessListener {
                    _loadingStatus.value = false
                    _savedMessage.value = "Product saved in storage"
                }
                .addOnFailureListener {
                    _loadingStatus.value = false
                    Log.e("error_tag", it.message.toString())
                    _savedMessage.value = it.message.toString()
                }
        }
    }

}