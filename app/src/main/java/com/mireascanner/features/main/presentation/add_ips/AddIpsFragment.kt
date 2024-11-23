package com.mireascanner.features.main.presentation.add_ips

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mireascanner.R
import com.mireascanner.common.main.domain.models.AddIp
import com.mireascanner.common.ui.showErrorSnackbar
import com.mireascanner.databinding.FragmentAddIpsBinding
import com.mireascanner.features.main.presentation.add_ips.list.AddIpsAdapter
import kotlinx.coroutines.launch

class AddIpsFragment : Fragment() {

    private var _binding: FragmentAddIpsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddIpsViewModel>()
    private lateinit var adapter: AddIpsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddIpsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AddIpsAdapter(::onDeleteItem)
        binding.ipsRecyclerview.adapter = adapter
        binding.ipsRecyclerview.layoutManager = LinearLayoutManager(context)
        observeViewModel()
        initUI()
    }

    private fun initUI() {
        binding.addButton.setOnClickListener {
            if (checkIp(binding.etIp.text.toString())) {
                viewModel.addIp(binding.etIp.text.toString())
            } else {
                showErrorSnackbar(requireContext(), requireView(), R.string.ip_error)
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ipsState.collect {
                    Log.d("IPs", it.toString())
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun onDeleteItem(addIp: AddIp) {
        viewModel.removeAddIp(addIp)
    }

    private fun checkIp(ip: String): Boolean {
        val ipWithSubnetRegex =
            Regex("^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])/(3[0-2]|[12]?[0-9])$")
        val ipOnlyRegex =
            Regex("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
        val ipRangeRegex = Regex("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\-(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
        return ipWithSubnetRegex.matches(ip) || ipOnlyRegex.matches(ip) || ipRangeRegex.matches(ip)
    }
}