package com.dvl.sigma.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.dvl.sigma.R
import com.dvl.sigma.data.models.responses.Response
import com.dvl.sigma.databinding.ActivityLoginBinding
import com.dvl.sigma.ui.base.BaseActivity
import com.dvl.sigma.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener {

    val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        initUI()
    }

    private fun initUI() {
        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.btnLogin) {
            showProgress()
            viewModel.login().observe(this, Observer {

                when (it) {
                    is Response.Loading -> {
                        Log.e(TAG, "Loading...")

                    }
                    is Response.Success -> {
                        Log.e(TAG, "Success...")
                        Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT).show()
                        hideProgress()
                        openHomeActivity()
                    }
                    is Response.Error -> {
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                        hideProgress()
                        Log.e(TAG, "Error == ${it.errorMessage}")
                    }
                }

            })
        }
    }

    private fun openHomeActivity() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}