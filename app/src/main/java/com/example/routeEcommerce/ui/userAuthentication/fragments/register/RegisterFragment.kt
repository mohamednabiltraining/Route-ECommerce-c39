package com.example.routeEcommerce.ui.userAuthentication.fragments.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.routeEcommerce.Constants
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.BaseFragment
import com.example.routeEcommerce.databinding.FragmentRegisterBinding
import com.example.routeEcommerce.ui.home.activity.MainActivity
import com.example.routeEcommerce.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.route.domain.models.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>() {
    private val mViewModel: RegisterContract.RegisterViewModel by viewModels<RegisterViewModel>()

    override fun initViewModel(): RegisterViewModel {
        return mViewModel as RegisterViewModel
    }

    override fun getLayoutId(): Int = R.layout.fragment_register

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
        initViews()
        observeData()
    }

    private fun observeData() {
        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                is RegisterContract.Event.ErrorMessage -> {
                    showErrorView(it.message.message)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    RegisterContract.State.Registering -> {
                        showLoadingView()
                    }

                    is RegisterContract.State.Registered -> {
                        showSuccessView()
                        delay(2000)
                        navigateToHome(state.user)
                    }

                    RegisterContract.State.Pending -> {}
                }
            }
        }
    }

    private fun initViews() {
        var isClicked = false
        binding.registerBtn.setOnClickListener {
            // register
            if (isClicked) return@setOnClickListener
            isClicked = true
            viewModel.doAction(RegisterContract.Action.Register)
        }
        binding.loginDoHaveAccountTv.setOnClickListener {
            // go to login
            navigateToLogin()
        }
    }

    private fun showSuccessView() {
        binding.icNext.isVisible = true
        binding.progressBar.isVisible = false
    }

    private fun showErrorView(message: String) {
        binding.icNext.isVisible = true
        binding.progressBar.isVisible = false
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .setBackgroundTint(requireContext().getColor(R.color.white))
            .show()
    }

    private fun showLoadingView() {
        binding.icNext.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    private fun navigateToHome(user: User) {
        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra(Constants.PARSE_USER_DATA, user)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun hideKeyboard() {
        view?.hideKeyboard(activity as AppCompatActivity?)
    }
}
