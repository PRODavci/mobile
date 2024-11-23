package com.mireascanner

import NotificationsPermissionHelper
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.mireascanner.common.permissions.PermissionsSharedPreferencesManager
import com.mireascanner.databinding.ActivityMainBinding
import com.mireascanner.splash.MainState
import com.mireascanner.splash.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setupSplashScreen()
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupNavigation()
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        val notificationHelper = NotificationsPermissionHelper(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationHelper.checkAndRequestPermission(this)
        }
        return super.onCreateView(parent, name, context, attrs)
    }

    private fun setupSplashScreen() {
        var keepSplashScreen = true
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.state.collect { state ->
                    keepSplashScreen = when (state) {
                        is MainState.Loading -> true

                        is MainState.ContentState -> false
                    }
                }
            }
        }

        installSplashScreen().setKeepOnScreenCondition {
            keepSplashScreen
        }
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.state.collect { state ->
                    val navGraph =
                        navController.navInflater.inflate(R.navigation.global_navigation_graph)
                    navGraph.setStartDestination(
                        when (state) {
                            is MainState.Loading -> {
                                R.id.authFlowFragment
                            }

                            is MainState.ContentState -> {
                                if (state.isAuthorized) {
                                    R.id.mainFlowFragment
                                } else {
                                    R.id.authFlowFragment
                                }
                            }
                        }
                    )

                    navController.graph = navGraph
                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NotificationsPermissionHelper.PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val permissionsSharedPreferencesManager = PermissionsSharedPreferencesManager()
                    permissionsSharedPreferencesManager.putLocationPermissionFlag(
                        applicationContext,
                        false
                    )
                }
                Log.d("PermTag", "yes")
            } else {
                Log.d("PermTag", "no")
            }
        }
    }
}