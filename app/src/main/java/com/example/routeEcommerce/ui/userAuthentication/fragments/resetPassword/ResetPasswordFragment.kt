package com.example.routeEcommerce.ui.userAuthentication.fragments.resetPassword

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.BaseFragment
import com.example.routeEcommerce.base.showDialog
import com.example.routeEcommerce.databinding.FragmentResetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding, ResetPasswordViewModel>() {
    private val mViewModel: ResetPasswordContract.ResetPasswordViewModel by viewModels<ResetPasswordViewModel>()
    private val args: ResetPasswordFragmentArgs by navArgs()

    override fun initViewModel(): ResetPasswordViewModel {
        return mViewModel as ResetPasswordViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_reset_password
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
        with(binding) {
            vm = viewModel
            updatePassword.setOnClickListener {
                viewModel.doAction(ResetPasswordContract.Action.UpdatePassword(args.userEmail))
            }
        }
        binding.lifecycleOwner = this
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    ResetPasswordContract.State.FailedToUpdatePassword -> returnToLoginFragment()
                    ResetPasswordContract.State.Logging -> showLoadingView()
                    is ResetPasswordContract.State.PasswordUpdated -> navToLoginFragment()
                    ResetPasswordContract.State.Pending -> showIdleView()
                }
            }
        }
    }

    private fun navToLoginFragment() {
        showIdleView()
        showDialog(
            message = getString(R.string.password_changed_successfully),
            posActionName = getString(R.string.back_to_login),
            posActionCallBack = { findNavController().popBackStack() },
        )
    }

    private fun showIdleView() {
        with(binding) {
            updatePassword.visibility = View.VISIBLE
            loading.isGone = true
        }
    }

    private fun showLoadingView() {
        with(binding) {
            loading.visibility = View.VISIBLE
            updatePassword.isGone = true
        }
    }

    private fun returnToLoginFragment() {
        showDialog(
            message = getString(R.string.something_went_wrong_please_try_again_later_if_this_keep_happening_call_the_support_team),
            posActionName = getString(R.string.go_back),
            posActionCallBack = {
                findNavController().popBackStack()
            },
        )
    }
}
