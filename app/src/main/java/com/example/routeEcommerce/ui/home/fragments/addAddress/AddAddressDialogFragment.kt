package com.example.routeEcommerce.ui.home.fragments.addAddress

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.showDialog
import com.example.routeEcommerce.databinding.FragmentAddAddressDialogBinding
import com.example.routeEcommerce.ui.home.fragments.profile.ProfileFragment.Companion.BUNDLE_KEY
import com.example.routeEcommerce.ui.home.fragments.profile.ProfileFragment.Companion.REQUEST_KEY
import com.example.routeEcommerce.ui.userAuthentication.activity.UserAuthenticationActivity
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils
import com.example.routeEcommerce.utils.parseJson
import com.example.routeEcommerce.utils.showSnackBar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddAddressDialogFragment : BottomSheetDialogFragment() {
    private val viewModel: AddAddressContract.ViewModel by viewModels<AddAddressViewModel>()
    private var _binding: FragmentAddAddressDialogBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        val citiesList =
            parseJson(requireContext()).map {
                it.city
            }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_city_drop_down, citiesList)
        binding.citiesDropDown.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddAddressDialogBinding.inflate(inflater, container, false)
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

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    AddAddressContract.State.Idle -> idleView()
                    AddAddressContract.State.Loading -> showLoading()
                    AddAddressContract.State.Success -> showSuccess()
                    is AddAddressContract.State.Error -> {
                        Log.e("Error->", "$state.message.message")
                        showDialog(state.message)
                        setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to false))
                    }
                }
            }
        }
    }

    private fun idleView() {
        binding.loading.isGone = true
        binding.addAddress.isGone = false
    }

    private fun showSuccess() {
        showSnackBar("Address added successfully")
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to true))
    }

    private fun showLoading() {
        binding.loading.isGone = false
        binding.addAddress.isGone = true
    }

    private fun initView() {
        binding.vm = viewModel as AddAddressViewModel
        binding.lifecycleOwner = this
        binding.addAddress.setOnClickListener {
            val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)
            if (token != null) {
                viewModel.doAction(AddAddressContract.Action.AddNewAddress(token))
            } else {
                showDialog(
                    message = "Login Again",
                    posActionName = "Go login",
                    posActionCallBack = {
                        startActivity(
                            Intent(
                                requireActivity(),
                                UserAuthenticationActivity::class.java,
                            ),
                        )
                        requireActivity().finish()
                    },
                    isCancelable = false,
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
