package com.mireascanner.features.main.presentation.main

import NotificationsPermissionHelper
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mireascanner.R
import com.mireascanner.common.main.domain.models.Scan
import com.mireascanner.common.navigation.activityNavController
import com.mireascanner.common.ui.LoadingDialog
import com.mireascanner.common.ui.showErrorSnackbar
import com.mireascanner.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var notificationsPermissionHelper: NotificationsPermissionHelper

    private lateinit var loaderDialog: LoadingDialog

    private val UPDATE_INTERVAL = 1 * 60 * 1000L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        initUi()
        observeViewModel()
        observeEffect()
        viewModel.handleAction(MainAction.GetAllScans)
        notificationsPermissionHelper = NotificationsPermissionHelper(requireActivity())
        return binding.root
    }

    private fun initUi() {
        loaderDialog = LoadingDialog(requireContext())
        loaderDialog.show()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            binding.includeNotificaiton.root.visibility = View.GONE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            binding.includeNotificaiton.root.setOnClickListener {
                notificationsPermissionHelper.checkAndRequestPermission(requireActivity())
            }

            binding.includeNotificaiton.btnOnNotifications.setOnClickListener {
                notificationsPermissionHelper.checkAndRequestPermission(requireActivity())
            }
        }

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_addIpsFragment)
        }

        binding.mainRefresh.setOnRefreshListener {
            viewModel.handleAction(MainAction.GetAllScans)
        }

    }

    private fun observeEffect() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect {
                    Log.d("MainFragment", it.toString())
                    when (it) {
                        is MainEffect.ShowLoader -> {
                            loaderDialog.show()
                        }

                        is MainEffect.CancelLoader -> {
                            loaderDialog.dismiss()
                        }

                        is MainEffect.NavigateToAuth -> {
                            activityNavController().navigate(R.id.action_global_authFlowFragment)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    Log.d("MainFragment", state.toString())
                    if(state.error != null){
                        showErrorSnackbar(
                            requireContext(),
                            binding.root,
                            state.error.asString(requireContext())
                        )
                    }
                    if(state.scans != null){
                        initAdapter(state.scans)
                    }
                }
            }
        }
    }

    private fun initAdapter(scans: Array<Scan>){
        binding.rvIps.adapter = ScansAdapter(requireContext(), scans) { host ->
            val b = Bundle()
            b.putIntArray("HostsIds", host.hosts.map { it.id }.toIntArray())
            b.putStringArray("HostsIps", host.hosts.map { it.ip }.toTypedArray())
            findNavController().navigate(R.id.action_mainScreenFragment_to_scanFragment, b)
        }
        binding.rvIps.layoutManager = LinearLayoutManager(requireContext())

        binding.mainRefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            binding.includeNotificaiton.root.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationsPermissionHelper.onCleared()
    }
}