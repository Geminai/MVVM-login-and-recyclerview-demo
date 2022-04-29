package com.dvl.sigma.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.dvl.sigma.utils.widgets.CustomProgress

abstract class BaseActivity : AppCompatActivity() {

    private var customProgress: CustomProgress? = null


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }

    fun showProgress() {
        customProgress = CustomProgress(this)
        customProgress?.show()
    }

    fun hideProgress() {
        customProgress?.hide()
        customProgress?.dismiss()
        customProgress?.cancel()
        customProgress = null
    }

}