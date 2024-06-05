package com.example.routeEcommerce.ui.home.fragments.checkOut

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.BaseFragment
import com.example.routeEcommerce.base.showDialog
import com.example.routeEcommerce.databinding.FragmentCheckOutBinding
import com.example.routeEcommerce.ui.home.fragments.checkOut.adapter.AddressViewPagerAdapter
import com.example.routeEcommerce.ui.userAuthentication.activity.UserAuthenticationActivity
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckOutFragment : BaseFragment<FragmentCheckOutBinding, CheckOutViewModel>() {
    private val mViewModel: CheckOutContract.CheckOutViewModel by viewModels<CheckOutViewModel>()

    private val addressesAdapter by lazy { AddressViewPagerAdapter() }

    override fun getLayoutId(): Int {
        return R.layout.fragment_check_out
    }

    override fun initViewModel(): CheckOutViewModel {
        return mViewModel as CheckOutViewModel
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initView()
        loadUserAddresses()
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is CheckOutContract.State.Error -> showDialog(state.errorMessage)
                    CheckOutContract.State.Loading -> showLoading()
                    is CheckOutContract.State.Success -> {
                        initView()
                        addressesAdapter.bindCategories(state.addresses)
                    }
                }
            }
        }
    }

    private fun loadUserAddresses() {
        val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)
        if (token != null) {
            viewModel.doAction(CheckOutContract.Action.LoadUserAddresses(token))
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

    private fun showLoading() {
        binding.selectPaymentView.visibility = View.VISIBLE
        binding.paymentSection.visibility = View.INVISIBLE
        binding.loadingIndicator.visibility = View.VISIBLE
    }

    private fun initView() {
        binding.paymentSection.visibility = View.VISIBLE
        binding.orderConfirmed.visibility = View.GONE
        binding.loadingIndicator.visibility = View.GONE
        binding.addressesRv.adapter = addressesAdapter

        binding.confirmButton.setOnClickListener {
            binding.selectPaymentView.visibility = View.GONE
            binding.orderConfirmed.visibility = View.VISIBLE
        }
    }
}
