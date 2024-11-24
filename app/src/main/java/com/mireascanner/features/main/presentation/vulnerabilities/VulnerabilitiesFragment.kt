package com.mireascanner.features.main.presentation.vulnerabilities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mireascanner.common.main.data.remote.model.MetaResponse
import com.mireascanner.common.main.data.remote.model.MetricsResponse
import com.mireascanner.common.main.data.remote.model.VulnerabilityResponse
import com.mireascanner.databinding.FragmentVulnerabilitiesBinding

class VulnerabilitiesFragment : Fragment() {

    private val viewModel by viewModels<VulnerabilitiesViewModel>()

    private var _binding: FragmentVulnerabilitiesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVulnerabilitiesBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        val description = arguments?.getStringArray("Description")
        val product = arguments?.getString("Product")
        val baseScores = arguments?.getFloatArray("BaseScore")
        val refs = arguments?.getStringArray("Refs")
        if(description != null && product != null && baseScores != null && refs != null){
            val vuln = mutableListOf<VulnerabilityResponse>()
            for(i in description.indices){
                vuln.add(
                    VulnerabilityResponse(
                        null,
                        baseScores[i],
                        description[i],
                        refs[i].split(", "),
                        product,
                    )
                )
            }
            val adapter = VulnerabilitiesAdapter(vuln)
            binding.rvVulnerabilities.adapter = adapter
            binding.rvVulnerabilities.layoutManager = LinearLayoutManager(requireContext())
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}