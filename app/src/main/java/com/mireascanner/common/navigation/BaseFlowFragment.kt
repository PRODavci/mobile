package com.mireascanner.common.navigation

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

abstract class BaseFlowFragment(
    @LayoutRes layoutId: Int,
) : Fragment(layoutId) {

    protected lateinit var navController: NavController

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.fragments[0] as NavHostFragment
        navController = navHostFragment.navController

        setupNavigation()
    }

    protected open fun setupNavigation() {
    }
}