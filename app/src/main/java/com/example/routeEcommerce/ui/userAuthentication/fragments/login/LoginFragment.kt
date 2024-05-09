package com.example.routeEcommerce.ui.userAuthentication.fragments.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.routeEcommerce.R
import com.example.routeEcommerce.databinding.FragmentLoginBinding
import com.example.routeEcommerce.ui.home.activity.MainActivity
import com.example.routeEcommerce.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {
    private lateinit var viewBinding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
        initViews()
    }

    private fun initViews() {
        viewBinding.loginBtn.setOnClickListener {
            // login
        }
        viewBinding.donTHaveAnAccountTv.setOnClickListener {
            // go to register
            navigateToRegister()
        }
    }

    private fun showSuccessView() {
        viewBinding.icNext.isVisible = true
        viewBinding.progressBar.isVisible = false
    }

    private fun showErrorView(message: String) {
        viewBinding.icNext.isVisible = true
        viewBinding.progressBar.isVisible = false
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .setBackgroundTint(resources.getColor(R.color.white))
            .show()
    }

    private fun showLoadingView() {
        viewBinding.icNext.isVisible = false
        viewBinding.progressBar.isVisible = true
    }

    private fun navigateToRegister() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun navigateToHome() {
        startActivity(Intent(activity, MainActivity::class.java))
        requireActivity().finish()
    }

    private fun hideKeyboard() {
        view?.hideKeyboard(activity as AppCompatActivity?)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.unbind()
    }
}
