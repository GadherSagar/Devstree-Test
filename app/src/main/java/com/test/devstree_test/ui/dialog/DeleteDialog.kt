package com.test.devstree_test.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import com.test.devstree_test.databinding.DeleteDialogBinding

class DeleteDialog(ctx: Context, var listener: () -> Unit) {
    private var _binding: DeleteDialogBinding? = null
    val binding get() = _binding!!

    private val dialog: Dialog = Dialog(ctx)

    init {
        _binding = DeleteDialogBinding.inflate(LayoutInflater.from(ctx), null, false)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(binding.root)


        binding.btnOkay.setOnClickListener {
            listener.invoke()
            closeDialog()
        }
        binding.btnCancel.setOnClickListener {
            closeDialog()
        }
    }

    fun showDialog() {
        dialog.show()
    }

    private fun closeDialog() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }
}