package com.example.routeEcommerce.ui.userAuthentication.fragments.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.BaseFragment
import com.example.routeEcommerce.databinding.FragmentLoginBinding
import com.example.routeEcommerce.ui.home.activity.MainActivity
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils
import com.example.routeEcommerce.utils.hideKeyboard
import com.example.routeEcommerce.utils.showSnackBar
import com.route.domain.models.AuthResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    private val mViewModel: LoginContract.LoginViewModel by viewModels<LoginViewModel>()

    override fun initViewModel(): LoginViewModel {
        return mViewModel as LoginViewModel
    }

    override fun getLayoutId(): Int = R.layout.fragment_login

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
        observeData()
        initViews()
    }

    private fun observeData() {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is LoginContract.Event.ErrorMessage -> {
                    Log.e("authFailed->", "${event.message.message}")
                    showErrorView(event.message.message)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is LoginContract.State.Logged -> navigateToHome(state.response)
                    LoginContract.State.Logging -> showLoadingView()
                    LoginContract.State.Pending -> showSuccessView()
                }
            }
        }
    }

    private fun initViews() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.loginBtn.setOnClickListener {
            // login
            viewModel.doAction(LoginContract.Action.Login)
        }
        binding.donTHaveAnAccountTv.setOnClickListener {
            // go to register
            navigateToRegister()
        }
    }

    private fun showSuccessView() {
        binding.icNext.isVisible = true
        binding.progressBar.isVisible = false
    }

    private fun showErrorView(message: String) {
        binding.icNext.isVisible = true
        binding.progressBar.isVisible = false
        showSnackBar(message)
    }

    private fun showLoadingView() {
        binding.icNext.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun navigateToRegister() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun navigateToHome(userData: AuthResponse) {
        UserDataUtils().saveUserInfo(requireContext(), UserDataFiled.TOKEN, userData.token)
        UserDataUtils().saveUserInfo(requireContext(), UserDataFiled.ROLE, userData.user?.role)
        UserDataUtils().saveUserInfo(requireContext(), UserDataFiled.NAME, userData.user?.name)
        UserDataUtils().saveUserInfo(requireContext(), UserDataFiled.EMAIL, userData.user?.email)
        UserDataUtils().saveUserInfo(requireContext(), UserDataFiled.CART_ITEM_COUNT, null)
        startActivity(Intent(activity, MainActivity::class.java))
        requireActivity().finish()
    }

    private fun hideKeyboard() {
        view?.hideKeyboard(activity as AppCompatActivity?)
    }
}
