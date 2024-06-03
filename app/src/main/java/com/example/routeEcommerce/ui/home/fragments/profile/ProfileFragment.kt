package com.example.routeEcommerce.ui.home.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.BaseFragment
import com.example.routeEcommerce.base.showDialog
import com.example.routeEcommerce.databinding.FragmentProfileBinding
import com.example.routeEcommerce.ui.home.fragments.editProfile.EditEdition
import com.example.routeEcommerce.ui.home.fragments.profile.adapter.AddressViewPagerAdapter
import com.example.routeEcommerce.ui.splash.SplashActivity
import com.example.routeEcommerce.ui.userAuthentication.activity.UserAuthenticationActivity
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils
import com.route.domain.models.Address
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    private val mViewModel: ProfileContract.ProfileViewModel by viewModels<ProfileViewModel>()

    override fun initViewModel(): ProfileViewModel {
        return mViewModel as ProfileViewModel
    }

    override fun getLayoutId(): Int = R.layout.fragment_profile

    private val addressAdapter by lazy { AddressViewPagerAdapter() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initView()
        loadUserAddresses()
        extractResult()
    }

    private fun extractResult() {
        parentFragmentManager.setFragmentResultListener(REQUEST_KEY, this) { _, bundle ->
            val shouldRefresh = bundle.getBoolean(BUNDLE_KEY)
            when (shouldRefresh) {
                true -> loadUserAddresses()
                false -> {}
            }
        }
        parentFragmentManager.setFragmentResultListener(UPDATED_NAME_KEY, this) { _, bundle ->
            val shouldRefresh = bundle.getBoolean(IS_NAME_UPDATED)
            when (shouldRefresh) {
                true -> initView()
                false -> {}
            }
        }
    }

    private fun loadUserAddresses() {
        val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)
        if (token != null) {
            viewModel.doAction(ProfileContract.Action.LoadUserAddresses(token))
        } else {
            showDialog(
                message = "Login Again",
                posActionName = "Go login",
                posActionCallBack = {
                    startActivity(Intent(requireActivity(), UserAuthenticationActivity::class.java))
                    requireActivity().finish()
                },
                isCancelable = false,
            )
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    ProfileContract.State.Loading -> {
                        showLoading()
                    }

                    is ProfileContract.State.Success -> {
                        showSuccess(state.addresses)
                    }

                    is ProfileContract.State.Error -> showErrorView(state.errorMessage.message)
                }
            }
        }
    }

    private fun showSuccess(addresses: List<Address?>) {
        binding.errorView.isGone = true
        if (addresses.isEmpty()) {
            binding.emptyAddressList.isGone = false
            binding.loadingView.visibility = View.INVISIBLE
        } else {
            binding.emptyAddressList.isGone = true
            binding.loadingView.visibility = View.GONE
        }
        binding.successView.isVisible = true
        addressAdapter.bindCategories(addresses)
        initAdapter()
    }

    private fun showErrorView(message: String) {
        binding.loadingView.isGone = true
        binding.errorView.isVisible = true
        binding.successView.isGone = true
        binding.errorMessage.text = message
        binding.tryAgainBtn.setOnClickListener {
            loadUserAddresses()
        }
    }

    private fun showLoading() {
        binding.loadingView.isGone = false
        binding.errorView.isVisible = false
        binding.successView.isVisible = false
    }

    private fun initView() {
        binding.welcomeMessage.text =
            getString(
                R.string.welcome,
                UserDataUtils().getUserData(requireContext(), UserDataFiled.NAME)?.trim()
                    ?.split(Regex("\\s+"))?.get(0),
            )
        binding.emailText.text = UserDataUtils().getUserData(requireContext(), UserDataFiled.EMAIL)
        binding.editProfileBtn.setOnClickListener {
            navToEditProfile(EditEdition.UPDATE_NAME)
        }
        binding.addNewAddressBtn.setOnClickListener {
            navToAddAddressBottomSheet()
        }
        binding.changePasswordBtn.setOnClickListener {
            navToEditProfile(EditEdition.UPDATE_PASSWORD)
        }
        binding.signOutBtn.setOnClickListener {
            signOut()
        }
    }

    private fun navToEditProfile(editType: EditEdition) {
        val action =
            ProfileFragmentDirections.actionProfileFragmentToEditProfileDialogFragment(editType)
        findNavController().navigate(action)
    }

    private fun navToAddAddressBottomSheet() {
        val action = ProfileFragmentDirections.actionProfileFragmentToAddAddressDialogFragment()
        findNavController().navigate(action)
    }

    private fun initAdapter() {
        binding.addressesVp.adapter = addressAdapter
        binding.dotsIndicator.attachTo(binding.addressesVp)
        addressAdapter.deleteAddress = { address ->
            showDialog(
                message = "Are you sure you want to delete this address?",
                posActionName = "Delete",
                posActionCallBack = {
                    getToken()?.let { token ->
                        viewModel.doAction(
                            ProfileContract.Action.DeleteAddress(
                                token,
                                address.id!!,
                            ),
                        )
                    }
                },
                negActionName = "Cancel",
                negActionCallBack = { return@showDialog },
                isCancelable = true,
            )
        }
    }

    private fun getToken(): String? {
        val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)
        if (token == null) {
            showDialog(
                message = "Login Again",
                posActionName = "Go login",
                posActionCallBack = {
                    startActivity(Intent(requireActivity(), UserAuthenticationActivity::class.java))
                    requireActivity().finish()
                },
                isCancelable = false,
            )
            return null
        }
        return token
    }

    private fun signOut() {
        UserDataUtils().saveUserInfo(requireContext(), UserDataFiled.TOKEN, null)
        UserDataUtils().saveUserInfo(requireContext(), UserDataFiled.CART_ITEM_COUNT, null)
        startActivity(Intent(requireActivity(), SplashActivity::class.java))
        requireActivity().finish()
    }

    companion object {
        const val REQUEST_KEY = "resultKey"
        const val BUNDLE_KEY = "isDismissed"

        const val UPDATED_NAME_KEY = "updatedName"
        const val IS_NAME_UPDATED = "isNameUpdated"
    }
}
