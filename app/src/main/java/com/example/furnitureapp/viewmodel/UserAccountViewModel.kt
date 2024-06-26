package com.example.furnitureapp.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.media.MediaMetadata
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.furnitureapp.FurnitureApplication
import com.example.furnitureapp.data.User
import com.example.furnitureapp.util.Constants.USER_COLLECTION
import com.example.furnitureapp.util.RegisterValidation
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.util.validateEmail
import com.example.furnitureapp.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: StorageReference,
    app: Application
) : AndroidViewModel(app) {

    private val _user = MutableStateFlow<Resource<User>>(Resource.Initial())
    val user = _user.asStateFlow()

    private val _updateInfo = MutableStateFlow<Resource<User>>(Resource.Initial())
    val updateInfo = _updateInfo.asStateFlow()

    init {
        getUser()
    }

    fun getUser() {
        _user.value = Resource.Loading()
        firestore.collection(USER_COLLECTION)
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                val result = it.toObject(User::class.java)
                result?.let { user ->
                    _user.value = Resource.Success(data = user)
                }
            }
            .addOnFailureListener {
                _user.value = Resource.Error(message = it.message.toString())
            }
    }

    fun updateUser(user: User, imageUri: Uri?) {
        val inputValid = validateEmail(user.email) is RegisterValidation.Success
                && user.firstName.trim().isNotEmpty()
                && user.lastName.trim().isNotEmpty()

        if (!inputValid) {
            _updateInfo.value = Resource.Error("Check your inputs")
            return
        }

        _updateInfo.value = Resource.Loading()

        if (imageUri == null) {
            saveUserInformation(user, true)
        } else {
            saveUserInformationWithNewImage(user, imageUri)
        }

    }

    private fun saveUserInformationWithNewImage(user: User, imageUri: Uri) {
        viewModelScope.launch {
            try {
                val imageBitmap = MediaStore.Images.Media.getBitmap(
                    getApplication<FurnitureApplication>().contentResolver,
                    imageUri
                )
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutputStream)
                val imageByteArray = byteArrayOutputStream.toByteArray()
                val imageDirectory = storage.child("profile_images/${auth.uid}/${UUID.randomUUID()}")
                val result = imageDirectory.putBytes(imageByteArray).await()
                val imageUrl = result.storage.downloadUrl.await().toString()
                saveUserInformation(user.copy(imagePath = imageUrl), false)
            } catch (ex: Exception) {
                _updateInfo.value = Resource.Error(message = ex.message.toString())
            }
        }
    }

    private fun saveUserInformation(user: User, shouldRetrievedOldImage: Boolean) {
        firestore
            .runTransaction { transaction ->
                val documentRef = firestore.collection(USER_COLLECTION).document(auth.uid!!)
                val currentUser = transaction.get(documentRef).toObject(User::class.java)
                if (shouldRetrievedOldImage && currentUser != null) {
                    val newUser = user.copy(imagePath = currentUser.imagePath)
                    transaction.set(documentRef, newUser)
                } else {
                    transaction.set(documentRef, user)
                }
            }
            .addOnSuccessListener {
                _updateInfo.value = Resource.Success(data = user)
            }
            .addOnFailureListener {
                _updateInfo.value = Resource.Error(message = it.message.toString())
            }
    }

}