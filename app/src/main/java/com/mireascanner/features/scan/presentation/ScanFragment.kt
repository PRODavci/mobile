package com.mireascanner.features.scan.presentation

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mireascanner.R
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
        val hosts = arguments?.getStringArray("Hosts")
        if(hosts != null) {
            binding.rvHosts.adapter = HostsAdapter(hosts) {
                val b = Bundle()
                b.putString("Host", it)
                findNavController().navigate(R.id.action_scanFragment_to_hostDetailsFragment, b)
            }
            binding.rvHosts.layoutManager = LinearLayoutManager(requireContext())
        }else{
            binding.rvHosts.visibility = View.GONE
            binding.tvThereIsNoHosts.visibility = View.VISIBLE
        }
    }
}