package com.example.routeEcommerce.ui.userAuthentication.fragments.verifyCode

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.BaseFragment
import com.example.routeEcommerce.base.showDialog
import com.example.routeEcommerce.databinding.FragmentVerifyResetCodeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VerifyResetCodeFragment :
    BaseFragment<FragmentVerifyResetCodeBinding, VerifyResetCodeViewModel>() {
    private val mViewModel: VerifyResetCodeContract.VerifyResetCodeViewModel by viewModels<VerifyResetCodeViewModel>()
    private val args: VerifyResetCodeFragmentArgs by navArgs()
    private val countDown = MyCountDownClass()

    override fun initViewModel(): VerifyResetCodeViewModel {
        return mViewModel as VerifyResetCodeViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_verify_reset_code
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
        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.canResend = false
        binding.resendCode.isClickable = false
        countDown.start()
        binding.verifyCodeBtn.setOnClickListener {
            viewModel.doAction(VerifyResetCodeContract.Action.VerifyResetCode)
        }
        binding.resendCode.setOnClickListener {
            binding.resendCode.isClickable = false
            viewModel.doAction(VerifyResetCodeContract.Action.ResendVerificationCode(args.userEmail))
        }
    }

    private fun observeData() {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                VerifyResetCodeContract.Event.ResendEmailFailed ->
                    showDialog(
                        message = getString(R.string.failed_to_send_the_code_please_try_again_later),
                        posActionName = getString(R.string.go_back),
                        posActionCallBack = { findNavController().popBackStack() },
                    )
            }
        }
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    VerifyResetCodeContract.State.Logging -> showLoading()
                    VerifyResetCodeContract.State.Pending -> idleView()
                    VerifyResetCodeContract.State.Verified -> navToResetPassword()
                    VerifyResetCodeContract.State.EmailSent -> emailSent()
                }
            }
        }
    }

    private fun emailSent() {
        binding.canResend = false
        binding.emailSentMessage.visibility = View.VISIBLE
        countDown.start()
        if (binding.countDownTimer.text == "01:20") {
            binding.emailSentMessage.isGone = true
        }
    }

    private fun idleView() {
        with(binding) {
            verifyCodeBtn.visibility = View.VISIBLE
            loading.isGone = true
        }
    }

    private fun showLoading() {
        with(binding) {
            loading.visibility = View.VISIBLE
            verifyCodeBtn.isGone = true
        }
    }

    private fun navToResetPassword() {
        countDown.cancel()
        val action =
            VerifyResetCodeFragmentDirections.actionVerifyResetCodeFragmentToResetPasswordFragment(
                args.userEmail,
            )
        findNavController().navigate(action)
    }

    inner class MyCountDownClass : CountDownTimer(120000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val minutes = millisUntilFinished / 60000
            val seconds = (millisUntilFinished % 60000) / 1000
            val formattedTime = String.format("%02d:%02d", minutes, seconds)
            binding.countDownTimer.text = formattedTime
        }

        override fun onFinish() {
            binding.countDownTimer.text = "00:00"
            binding.canResend = true
            binding.resendCode.isClickable = true
        }
    }
}
