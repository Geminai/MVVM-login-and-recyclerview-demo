package com.dvl.sigma.utils.widgets

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import com.dvl.sigma.databinding.PrograssBarDialogBinding


class CustomProgress (context: Context) : Dialog(context) {

    private lateinit var binding: PrograssBarDialogBinding
    private val TAG = "CustomProgress"

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = PrograssBarDialogBinding.inflate(layoutInflater)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        } catch (e: Exception) {
            Log.e(TAG, " Error in CustomProgress")
        }

    }

}