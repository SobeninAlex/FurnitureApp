package com.example.furnitureapp.dialog

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.furnitureapp.R
import com.example.furnitureapp.databinding.ResetPasswordDilogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupBottomSheetDialog(
    onSendClick: (String) -> Unit
) {
    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val inflater = LayoutInflater.from(requireContext())
    val binding = ResetPasswordDilogBinding.inflate(inflater)
    dialog.setContentView(binding.root)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    binding.btnSendResetPassword.setOnClickListener {
        val email = binding.edResetPassword.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    binding.btnCancelResetPassword.setOnClickListener {
        dialog.dismiss()
    }
}