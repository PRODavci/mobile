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
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mireascanner.MainActivity
import com.mireascanner.R
import com.mireascanner.common.auth.data.utils.Helper
import com.mireascanner.common.auth.domain.usecase.validate_password.ValidationPasswordResult
import com.mireascanner.common.navigation.navigateSafely
import com.mireascanner.common.ui.LoadingDialog
import com.mireascanner.common.ui.showErrorSnackbar
import com.mireascanner.databinding.FragmentSignUpBinding
import com.mireascanner.features.auth.presentation.sign_in.SignInState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val viewModel by viewModels<SignUpViewModel>()

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        observeViewModel()
        observeEffect()
        initUi()
        return binding.root
    }

    private fun initUi() {
        loadingDialog = LoadingDialog(requireContext())
        binding.btnSignUp.setOnClickListener {
            clearErrors()
            if (binding.etRegistrationPassword.text.toString() != binding.etRegistrationPasswordRepeat.text.toString()) {
                binding.inputLayoutRegistrationPasswordRepeat.error =
                    requireContext().getString(R.string.error_passwords_not_the_same)
                binding.inputLayoutRegistrationPassword.error =
                    requireContext().getString(R.string.error_passwords_not_the_same)
            } else {
                viewModel.handleAction(
                    SignUpAction.SignUp(
                        binding.etRegistrationEmail.text.toString(),
                        binding.etRegistrationPassword.text.toString()
                    )
                )
            }
        }

        binding.tvToLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.etRegistrationEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.handleAction(SignUpAction.EmailChanged(text.toString()))
        }

        binding.etRegistrationPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.handleAction(SignUpAction.PasswordChanged(text.toString()))
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    Log.d("SignUp", state.error?.asString(requireContext()) ?: "")
                    clearErrors()
                    if (state.error != null) {
                        showErrorSnackbar(
                            requireContext(),
                            binding.root,
                            state.error.asString(requireContext())
                        )
                    }
                    if (state.emailError != null) {
                        binding.inputLayoutRegistrationEmail.error =
                            state.emailError.asString(requireContext())
                    }
                    if (state.passwordError != null) {
                        binding.inputLayoutRegistrationPassword.error =
                            state.passwordError.asString(requireContext())
                    }
                    binding.btnSignUp.isEnabled = state.isSignUpButtonAvailable
                }
            }
        }
    }

    private fun observeEffect() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect { newEffect ->
                    Log.d("SingUpFragment", newEffect.toString())
                    when (newEffect) {
                        is SignUpEffect.ShowLoading -> {
                            loadingDialog.show()
                        }

                        is SignUpEffect.NavigateToMain -> {
                            findNavController().navigateSafely(R.id.action_global_mainFlowFragment)
                        }

                        is SignUpEffect.HideLoadingDialog -> {
                            loadingDialog.dismiss()
                        }
                    }
                }
            }
        }
    }

    private fun clearErrors() {
        binding.inputLayoutRegistrationPasswordRepeat.isErrorEnabled = false
        binding.inputLayoutRegistrationEmail.isErrorEnabled = false
        binding.inputLayoutRegistrationPassword.isErrorEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}