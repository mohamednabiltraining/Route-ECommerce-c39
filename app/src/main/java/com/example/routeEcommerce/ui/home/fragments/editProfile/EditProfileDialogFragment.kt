package com.example.routeEcommerce.ui.home.fragments.editProfile

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.routeEcommerce.R
import com.example.routeEcommerce.base.showDialog
import com.example.routeEcommerce.databinding.FragmentEditProfileDialogBinding
import com.example.routeEcommerce.ui.home.fragments.profile.ProfileFragment.Companion.IS_NAME_UPDATED
import com.example.routeEcommerce.ui.home.fragments.profile.ProfileFragment.Companion.UPDATED_NAME_KEY
import com.example.routeEcommerce.ui.userAuthentication.activity.UserAuthenticationActivity
import com.example.routeEcommerce.utils.UserDataFiled
import com.example.routeEcommerce.utils.UserDataUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.route.domain.models.AuthResponse
import com.route.domain.models.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileDialogFragment : BottomSheetDialogFragment() {
    private val viewModel: EditProfileContract.EditProfileViewModel by viewModels<EditProfileViewModel>()
    private var _binding: FragmentEditProfileDialogBinding? = null
    val binding get() = _binding!!

    private val args: EditProfileDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditProfileDialogBinding.inflate(inflater, container, false)
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
                    is EditProfileContract.State.Error -> showDialog(state.errorMessage)
                    EditProfileContract.State.Idle -> idle()
                    EditProfileContract.State.Loading -> showLoading()
                    is EditProfileContract.State.NameUpdated -> storeName(state.user)
                    is EditProfileContract.State.PasswordUpdated -> storeToken(state.authResponse)
                }
            }
        }
    }

    private fun storeToken(userData: AuthResponse) {
        Log.e("TAG", "storeToken: $userData")
        UserDataUtils().saveUserInfo(requireContext(), UserDataFiled.TOKEN, userData.token)
        showSnackBarr("Password Updated")
        dismiss()
    }

    private fun showSnackBarr(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .setTextColor(requireContext().getColor(R.color.gray))
            .setBackgroundTint(requireContext().getColor(R.color.white))
            .show()
    }

    private fun storeName(user: User?) {
        Log.e("TAG", "storeName: $user")
        UserDataUtils().saveUserInfo(requireContext(), UserDataFiled.NAME, user?.name)
        showSnackBarr("Name Updated")
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (args.editType == EditEdition.UPDATE_NAME) {
            setFragmentResult(UPDATED_NAME_KEY, bundleOf(IS_NAME_UPDATED to true))
        }
    }

    private fun idle() {
        binding.loading.isGone = true
        binding.updateInfo.isGone = false
    }

    private fun showLoading() {
        binding.loading.isGone = false
        binding.updateInfo.isGone = true
    }

    private fun initView() {
        binding.vm = viewModel as EditProfileViewModel
        binding.lifecycleOwner = this
        when (args.editType) {
            EditEdition.UPDATE_NAME -> {
                binding.isPasswordEdit = false
                setName()
                binding.updateInfo.setOnClickListener {
                    updateAccountInfo(EditEdition.UPDATE_NAME)
                }
            }

            EditEdition.UPDATE_PASSWORD -> {
                binding.isPasswordEdit = true
                binding.updateInfo.setOnClickListener {
                    updateAccountInfo(EditEdition.UPDATE_PASSWORD)
                }
            }
        }
    }

    private fun updateAccountInfo(editType: EditEdition) {
        val token = UserDataUtils().getUserData(requireContext(), UserDataFiled.TOKEN)
        if (token != null) {
            when (editType) {
                EditEdition.UPDATE_NAME -> {
                    viewModel.doAction(EditProfileContract.Action.UpdateAccountName(token))
                }

                EditEdition.UPDATE_PASSWORD -> {
                    viewModel.doAction(EditProfileContract.Action.UpdateAccountPassword(token))
                }
            }
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

    private fun setName() {
        val name = UserDataUtils().getUserData(requireContext(), UserDataFiled.NAME)
        (viewModel as EditProfileViewModel).currentUserName.value = name.toString()
        (viewModel as EditProfileViewModel).newUserName.value = name.toString()
        Log.e("TAG", "setName: $name")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
