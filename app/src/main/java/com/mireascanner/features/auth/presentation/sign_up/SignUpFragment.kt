package com.mireascanner.features.auth.presentation.sign_up

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mireascanner.MainActivity
import com.mireascanner.R
import com.mireascanner.common.navigation.navigateSafely
import com.mireascanner.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val viewModel by viewModels<SignUpViewModel>()

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        observeViewModel()
        initUi()
        return binding.root
    }

    private fun initUi() {
        binding.btnSignUp.setOnClickListener {
            binding.inputLayoutRegistrationEmail.isErrorEnabled = false
            binding.inputLayoutRegistrationPasswordRepeat.isErrorEnabled = false
            binding.inputLayoutRegistrationPassword.isErrorEnabled = false
            if(binding.etRegistrationPassword.text.toString() != binding.etRegistrationPasswordRepeat.text.toString()){
                binding.inputLayoutRegistrationPasswordRepeat.error = requireContext().getString(R.string.error_passwords_not_the_same)
                binding.inputLayoutRegistrationPassword.error = requireContext().getString(R.string.error_passwords_not_the_same)
            }else if(binding.etRegistrationPassword.text.toString().isBlank()){
                binding.inputLayoutRegistrationPassword.error = requireContext().getString(R.string.error_empty_field)
            }else if(binding.etRegistrationEmail.text.toString().isBlank()){
                binding.inputLayoutRegistrationEmail.error = requireContext().getString(R.string.error_empty_field)
            }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.etRegistrationEmail.text).matches()){
                binding.inputLayoutRegistrationEmail.error = requireContext().getString(R.string.error_incorrect_email)
            }else{
                viewModel.handleAction(SignUpAction.SignUp(
                    binding.etRegistrationEmail.text.toString(),
                    binding.etRegistrationPassword.text.toString()
                ))
            }
        }

        binding.tvToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is SignUpState.Succeed -> {
                            findNavController().navigateSafely(R.id.action_global_main_navigation_flow)
                        }

                        is SignUpState.Failed -> {
                            Toast.makeText(
                                requireContext(),
                                state.exception.message ?: requireContext().getString(R.string.error_something_went_wrong),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        is SignUpState.Loading -> {
                            (requireActivity() as MainActivity).showLoader()
                        }

                        is SignUpState.Content -> {
                            (requireActivity() as MainActivity).cancelLoader()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}