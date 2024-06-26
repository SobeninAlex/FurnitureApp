package com.example.furnitureapp.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<VB: ViewDataBinding> : Fragment() {

    private var _binding: VB? = null

    protected val binding get() = _binding ?: throw RuntimeException("binding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }

    fun showToast(message: String) {
        try {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
    }

    fun showSnackbar(message: String) {
        try {
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun getLayoutId(): Int

}