package com.example.routeEcommerce.ui.userAuthentication.fragments.forgetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.routeEcommerce.databinding.FragmentForgetPasswordBottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgetPasswordBottomDialogFragment : BottomSheetDialogFragment() {
    private val viewModel: ForgetPasswordContract.ForgetPasswordViewModel by viewModels<ForgetPasswordBottomDialogViewModel>()

    private var _binding: FragmentForgetPasswordBottomDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentForgetPasswordBottomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initView()
    }

    private fun initView() {
        binding.vm = viewModel as ForgetPasswordBottomDialogViewModel
        binding.lifecycleOwner = this
        binding.sendCodeBtn.setOnClickListener {
            viewModel.doAction(ForgetPasswordContract.Action.SendResetCode)
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    ForgetPasswordContract.State.EmailSent -> navigateToResetFragment()
                    ForgetPasswordContract.State.Idle -> showIdleView()
                    ForgetPasswordContract.State.Logging -> showLoadingView()
                }
            }
        }
    }

    private fun navigateToResetFragment() {
        val action =
            ForgetPasswordBottomDialogFragmentDirections.actionForgetPasswordBottomDialogFragmentToResetPasswordFragment(
                binding.emailText.text.toString(),
            )
        findNavController().navigate(action)
    }

    private fun showLoadingView() {
        binding.loading.visibility = View.VISIBLE
        binding.sendCodeBtn.isGone = true
    }

    private fun showIdleView() {
        binding.loading.isGone = true
        binding.sendCodeBtn.visibility = View.VISIBLE
    }
}
