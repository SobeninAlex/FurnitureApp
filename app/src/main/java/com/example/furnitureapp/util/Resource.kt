package com.example.furnitureapp.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Administrator<T> : Resource<T>()

    class Success<T>(data: T) : Resource<T>(data = data)

    class Error<T>(message: String) : Resource<T>(message = message)

    class Loading<T> : Resource<T>()

    class Initial<T> : Resource<T>()

}