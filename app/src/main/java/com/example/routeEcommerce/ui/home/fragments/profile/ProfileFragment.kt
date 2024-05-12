package com.example.routeEcommerce.ui.home.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.BaseFragment
import com.example.routeEcommerce.databinding.FragmentProfileBinding
import com.example.routeEcommerce.ui.splash.SplashActivity
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    override fun initViewModel(): ProfileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

    override fun getLayoutId(): Int = R.layout.fragment_profile

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.welcomeMessage.text =
            getString(
                R.string.welcome,
                UserDataUtils().getUserData(requireContext(), UserDataFiled.NAME)?.trim()
                    ?.split(Regex("\\s+"))?.get(0),
            )
        binding.emailText.text = UserDataUtils().getUserData(requireContext(), UserDataFiled.EMAIL)
        binding.signOutBtn.setOnClickListener {
            UserDataUtils().saveUserInfo(requireContext(), UserDataFiled.TOKEN, null)
            signOut()
        }
    }

    private fun signOut() {
        startActivity(Intent(requireActivity(), SplashActivity::class.java))
        requireActivity().finish()
    }
}
