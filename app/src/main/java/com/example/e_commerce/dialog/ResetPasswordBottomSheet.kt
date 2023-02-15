package com.example.e_commerce.dialog

import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.e_commerce.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupResetPasswordDialog(
    onSendEmailClick: (String) -> Unit
) {
    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view: View = layoutInflater.inflate(R.layout.layout_reset_password, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val etResetPassword = view.findViewById<EditText>(R.id.ETResetPassword)
    val btnCancel = view.findViewById<Button>(R.id.btnCancelResetPassword)
    val btnConfirm = view.findViewById<Button>(R.id.btnConfirmResetPassword)

    btnConfirm.setOnClickListener {
        val email = etResetPassword.text.toString().trim()
        if (!email.isEmpty()) {
            onSendEmailClick(email)
            etResetPassword.error = null
            dialog.dismiss()
        } else {
            etResetPassword.error = "من فضلك قم بإدخال الايميل !"
        }

    }
    btnCancel.setOnClickListener {
        dialog.dismiss()
    }

}