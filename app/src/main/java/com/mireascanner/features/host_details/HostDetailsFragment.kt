package com.mireascanner.features.host_details

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mireascanner.R

class HostDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = HostDetailsFragment()
    }

    private val viewModel: HostDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
    }

    private fun initUi() {
        val host = arguments?.getString("Host")
        if(host != null){

        }else{

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_host_details, container, false)
    }
}