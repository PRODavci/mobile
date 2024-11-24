package com.mireascanner.features.main.presentation.host_details

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mireascanner.R
import com.mireascanner.common.ui.LoadingDialog
import com.mireascanner.common.ui.showErrorSnackbar
import com.mireascanner.databinding.FragmentHostDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HostDetailsFragment : Fragment() {

    private var _binding: FragmentHostDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HostDetailsViewModel by viewModels()

    private lateinit var loadingDialog: LoadingDialog

    private var hostId: Int? = null

    private lateinit var adapter: HostServiceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHostDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeEffect()
        observeViewModel()
    }

    private fun observeEffect() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect {
                    Log.d("HostDetailsFragment", it.toString())
                    when (it) {
                        is HostDetailsEffect.ShowLoader -> {
                            loadingDialog.show()
                        }

                        is HostDetailsEffect.CancelLoader -> {
                            loadingDialog.dismiss()
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
                    Log.d("HostDetailsFragment", state.toString())
                    if (state.error != null) {
                        showErrorSnackbar(
                            requireContext(),
                            binding.root,
                            state.error.asString(requireContext())
                        )
                    } else if (state.details != null) {
                        Log.d("HostDetailsFragment", "here")
                        adapter.submitList(state.details.services)
                    }
                }
            }
        }
    }

    private fun initUi() {
        adapter = HostServiceAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        loadingDialog = LoadingDialog(requireContext())
        binding.hostDetailsRefresh.setOnRefreshListener {
            viewModel.handleAction(HostDetailsAction.GetHostDetails(hostId!!))
        }
        val hostIdArgument = arguments?.getInt("HostId")
        if (hostIdArgument != null) {
            hostId = hostIdArgument
            viewModel.handleAction(HostDetailsAction.GetHostDetails(hostId!!))
        } else {
            showErrorSnackbar(
                requireContext(),
                binding.root,
                R.string.error_something_went_wrong
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}