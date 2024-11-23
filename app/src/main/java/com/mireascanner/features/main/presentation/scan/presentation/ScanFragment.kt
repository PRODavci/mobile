package com.mireascanner.features.main.presentation.scan.presentation

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mireascanner.R
import com.mireascanner.common.main.domain.models.Host
import com.mireascanner.databinding.FragmentScanBinding

class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScanViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        initUi()
        return binding.root
    }

    private fun initUi() {
        val hostsIds = arguments?.getIntArray("HostsIds")
        val hostsIps = arguments?.getStringArray("HostsIps")
        if(hostsIps != null && hostsIds != null) {
            val hosts = arrayListOf<Host>()
            for(i in hostsIps.indices){
                hosts.add(Host(hostsIds[i], hostsIps[i]))
            }
            binding.rvHosts.adapter = HostsAdapter(hosts.toTypedArray()) {
                val b = Bundle()
                b.putInt("HostId", it)
                findNavController().navigate(R.id.action_scanFragment_to_hostDetailsFragment, b)
            }
            binding.rvHosts.layoutManager = LinearLayoutManager(requireContext())
        }else{
            binding.rvHosts.visibility = View.GONE
            binding.tvThereIsNoHosts.visibility = View.VISIBLE
        }
    }
}