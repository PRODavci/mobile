package com.mireascanner

import android.os.Bundle
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
import com.mireascanner.databinding.ActivityMainBinding
import com.mireascanner.features.splash.SplashState
import com.mireascanner.features.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val splashViewModel by viewModels<SplashViewModel>()

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

    private fun setupSplashScreen() {
        var keepSplashScreen = true
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.state.collect { state ->
                    keepSplashScreen = when (state) {
                        is SplashState.Loading -> true

                        is SplashState.ContentState -> false
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
                splashViewModel.state.collect { state ->
                    val navGraph =
                        navController.navInflater.inflate(R.navigation.global_navigation_graph)
                    navGraph.setStartDestination(
                        when (state) {
                            is SplashState.Loading -> {
                                R.id.authFlowFragment
                            }

                            is SplashState.ContentState -> {
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

}