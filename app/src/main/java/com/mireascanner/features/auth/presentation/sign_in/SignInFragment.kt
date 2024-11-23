package com.mireascanner.features.auth.presentation.sign_in

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mireascanner.R
import com.mireascanner.common.navigation.activityNavController
import com.mireascanner.common.navigation.navigateSafely
import com.mireascanner.common.ui.LoadingDialog
import com.mireascanner.common.ui.showErrorSnackbar
import com.mireascanner.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SignInViewModel>()

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        initUi()
        observeViewModel()
        observeEffect()
        return binding.root
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    clearErrors()
                    if (state.error != null) {
                        showErrorSnackbar(
                            requireContext(),
                            binding.root,
                            state.error.asString(requireContext())
                        )
                    }
                    if (state.emailError != null) {
                        binding.inputLayoutSignInEmail.error =
                            state.emailError.asString(requireContext())
                    }
                    binding.btnSignIn.isEnabled = state.isSignInButtonEnabled
                }
            }
        }
    }

    private fun observeEffect() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect { newEffect ->
                    when (newEffect) {
                        is SignInEffect.ShowLoading -> {
                            loadingDialog.show()
                        }

                        is SignInEffect.NavigateToMain -> {
                            activityNavController().navigateSafely(R.id.action_global_mainFlowFragment)
                        }

                        is SignInEffect.HideLoadingDialog -> {
                            loadingDialog.dismiss()
                        }
                    }
                }
            }
        }
    }

    private fun clearErrors() {
        binding.inputLayoutSignInEmail.isErrorEnabled = false
        binding.inputLayoutSignInPassword.isErrorEnabled = false
    }

    private fun initUi() {
        loadingDialog = LoadingDialog(requireContext())
        binding.tvToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_sign_in_to_sign_up)
        }

        binding.btnSignIn.setOnClickListener {
            clearErrors()
            viewModel.handleAction(
                SignInAction.SignIn(
                    binding.etSignInEmail.text.toString(),
                    binding.etSignInPassword.text.toString()
                )
            )
        }

        binding.etSignInEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.handleAction(SignInAction.EmailChanged(text.toString()))
        }

        binding.etSignInPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.handleAction(SignInAction.PasswordChanged(text.toString()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}