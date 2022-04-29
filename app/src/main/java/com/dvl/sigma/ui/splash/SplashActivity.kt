package com.dvl.sigma.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import com.dvl.sigma.R
import com.dvl.sigma.data.PreferencesManager
import com.dvl.sigma.databinding.ActivitySplashBinding
import com.dvl.sigma.ui.base.BaseActivity
import com.dvl.sigma.ui.home.HomeActivity
import com.dvl.sigma.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    lateinit var binding: ActivitySplashBinding
    private var isLoggedIn: Boolean = false

    private val DELAY_TIME = 3000

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // HERE WE ARE LUNCHING COROUTINE FOR GET LOGIN FLAG FROM DATA-STORE PREFERENCE
        // WE HAVE USED LIFE CYCLE SCOPE BECAUSE WE ARE IN ACTIVITY
        lifecycleScope.launch {
            isLoggedIn =
                preferencesManager.getValueFromKeyFlow(PreferencesManager.PreferencesKeys.IS_LOGIN)
                    .first()

        }

        initUI()
    }

    private fun initUI() {

        // HERE WE ARE TAKING THE REFERENCE OF OUR VIEW
        // SO THAT WE CAN PERFORM ANIMATION USING THAT VIEW
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        binding.tvAppName.startAnimation(slideAnimation)

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time and open next screen base on login flag value.
        Handler(Looper.getMainLooper()).postDelayed({
            if (isLoggedIn) {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
            finish()
        }, DELAY_TIME.toLong()) // 3000 is the delayed time in milliseconds.

    }

}