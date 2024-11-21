package com.mireascanner

import android.os.Bundle
import android.util.Log
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
import com.mireascanner.initial.MainState
import com.mireascanner.initial.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSplashScreen()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupNavigation()
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
                    val navGraph = navController.navInflater.inflate(R.navigation.global_navigation_graph)
                    Log.d("SignUpResult", state.toString())
                    navGraph.setStartDestination(
                        when (state) {
                            is MainState.Loading -> {
                                R.id.auth_navigation_flow
                            }

                            is MainState.ContentState -> {
                                if (state.isAuthorized) {
                                    R.id.main_navigation_flow
                                } else {
                                    R.id.auth_navigation_flow
                                }
                            }
                        }
                    )

                    navController.graph = navGraph
                }
            }
        }

    }
}