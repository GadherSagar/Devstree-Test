package com.test.devstree_test.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import com.test.devstree_test.R
import com.test.devstree_test.databinding.SortDialogBinding
import com.test.devstree_test.ui.util.Constant.SORT_ASCENDING
import com.test.devstree_test.ui.util.Constant.SORT_DESCENDING

class SortDialog(ctx: Context, sortType: String, var listener: (sortType: String) -> Unit) {
    private var _binding: SortDialogBinding? = null
    val binding get() = _binding!!

    private val dialog: Dialog = Dialog(ctx)

    init {
        _binding = SortDialogBinding.inflate(LayoutInflater.from(ctx), null, false)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(binding.root)

        if (sortType == SORT_ASCENDING) {
            binding.rbAscending.isChecked = true
        } else {
            binding.rbDescending.isChecked = true
        }

        binding.btnApply.setOnClickListener {
            when (binding.rgSort.checkedRadioButtonId) {
                R.id.rb_ascending -> {
                    listener.invoke(SORT_ASCENDING)
                }
                R.id.rb_descending -> {
                    listener.invoke(SORT_DESCENDING)
                }
            }
            closeDialog()
        }
        binding.btnClear.setOnClickListener {
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