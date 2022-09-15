package com.example.e_commerce.dialog

import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.e_commerce.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setUpDialogSheet(onSendClick: (String) -> Unit) {
    val bottomSheet = BottomSheetDialog(requireContext(),R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.item_reset_password, null)
    bottomSheet.setContentView(view)
    bottomSheet.behavior.state=BottomSheetBehavior.STATE_EXPANDED
    bottomSheet.show()

    val etEmail = view.findViewById<EditText>(R.id.et_reset_password)
    val btnReset = view.findViewById<Button>(R.id.btn_reset_password)
    val btnCancel = view.findViewById<Button>(R.id.btn_cancel)

    btnReset.setOnClickListener {
        val email = etEmail.text.toString().trim()
        onSendClick(email)
        bottomSheet.dismiss()
    }
    btnCancel.setOnClickListener {
        bottomSheet.dismiss()
    }
}