package com.example.furnitureapp.dialog

import android.app.AlertDialog
import androidx.fragment.app.Fragment

fun Fragment.setupAlertDialog(
    title: String,
    message: String,
    titlePositiveButton: String,
    titleNegativeButton: String,
    clickNegativeButton: (() -> Unit)? = null ,
    clickPositiveButton: (() -> Unit)? = null
) {
    val alertDialog = AlertDialog.Builder(requireContext()).apply {
        setTitle(title)
        setMessage(message)
        setNegativeButton(titleNegativeButton) { dialog, _ ->
            clickNegativeButton?.invoke()
            dialog.dismiss()
        }
        setPositiveButton(titlePositiveButton) { dialog, _ ->
            clickPositiveButton?.invoke()
            dialog.dismiss()
        }
    }
    alertDialog.create()
    alertDialog.show()
}