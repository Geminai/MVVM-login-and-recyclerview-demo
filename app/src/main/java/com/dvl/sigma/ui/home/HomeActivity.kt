package com.dvl.sigma.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.dvl.sigma.R
import com.dvl.sigma.data.models.responses.Response
import com.dvl.sigma.databinding.ActivityHomeBinding
import com.dvl.sigma.ui.base.BaseActivity
import com.dvl.sigma.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private val TAG = "HomeActivity"

    private lateinit var binding: ActivityHomeBinding

    private lateinit var navController: NavController

    val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawerLayout()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

    private fun setupDrawerLayout() {

        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white));
        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navController = navHostFragment!!.navController

        binding.navigationView.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)

        binding.navigationView.menu.getItem(2).setOnMenuItemClickListener {
            showProgress()

            viewModel.logout().observe(this, Observer {
                when (it) {
                    is Response.Loading -> {
                        Log.e(TAG, "Loading...")

                    }
                    is Response.Success -> {
                        Log.e(TAG, "Success...")
                        Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT).show()
                        hideProgress()
                        openLoginActivity()
                    }
                    is Response.Error -> {
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                        hideProgress()
                        Log.e(TAG, "Error == ${it.errorMessage}")
                    }
                }
            })
            return@setOnMenuItemClickListener true
        }

    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun openLoginActivity() {
        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}