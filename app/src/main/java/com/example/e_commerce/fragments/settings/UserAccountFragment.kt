package com.example.e_commerce.fragments.settings

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.e_commerce.R
import com.example.e_commerce.data.User
import com.example.e_commerce.databinding.FragmentProfileBinding
import com.example.e_commerce.databinding.FragmentUserAccountBinding
import com.example.e_commerce.dialog.setupResetPasswordDialog
import com.example.e_commerce.mvvm.LoginViewModel
import com.example.e_commerce.mvvm.UserAccountViewModel
import com.example.e_commerce.utils.Resources
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class UserAccountFragment : Fragment() {
    private lateinit var binding: FragmentUserAccountBinding
    private val viewModel by viewModels<UserAccountViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()

    private var imageUri: Uri? = null

    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                imageUri = it.data?.data
                Glide.with(this@UserAccountFragment).load(imageUri)
                    .error(R.drawable.ic_person).into(binding.profileImageUser)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUserState()
        collectUpdateInfoState()

        binding.profileBtnSave.setOnClickListener {
            binding.apply {
                val fName = profileFirstNameEt.text.trim().toString()
                val lName = profileLastNameEt.text.trim().toString()
                val email = profileEmailEt.text.trim().toString()
                val user = User(fName, lName, email)
                viewModel.updateUser(user, imageUri)
            }
        }
        binding.profileImageEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imageActivityResultLauncher.launch(intent)
        }
        binding.profileForgetPasswordTxt.setOnClickListener {
            setupResetPasswordDialog { email ->
                loginViewModel.resetPassword(email)

            }
        }
        binding.profileCloseIcon.setOnClickListener { findNavController().popBackStack() }
    }


    private fun collectUpdateInfoState() {
        lifecycleScope.launchWhenStarted {
            viewModel.updateInfo.collectLatest {
                when (it) {
                    is Resources.Error -> {
                        binding.profileBtnSave.revertAnimation()
                        Toasty.error(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resources.Loading -> {
                        binding.profileBtnSave.startAnimation()
                    }
                    is Resources.Success -> {
                        binding.profileBtnSave.revertAnimation()
                        findNavController().popBackStack()
                    }
                    else -> Unit
                }
            }
            loginViewModel.resetPassword.collect {
                when (it) {
                    is Resources.Error -> {
                        binding.profileSpinKit.visibility = View.GONE
                        Toasty.error(requireContext(), "some error happen ! try again")
                    }
                    is Resources.Loading -> {
                        binding.profileSpinKit.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.profileSpinKit.visibility = View.GONE
                        Snackbar.make(
                            requireView(),
                            "تم إرسال رابط تغيير كلمة السر من فضلك تصفح إيميلك",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun collectUserState() {
        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when (it) {
                    is Resources.Error -> {
                        Toasty.error(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resources.Loading -> {
                        showUserLoading()
                    }
                    is Resources.Success -> {
                        hideUserLoading()
                        showUserInformation(it.data!!)

                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showUserInformation(user: User) {
        binding.apply {
            Glide.with(this@UserAccountFragment).load(user.imagePath)
                .error(ColorDrawable(Color.BLACK))
                .into(profileImageUser)
            profileFirstNameEt.setText(user.firstName)
            profileLastNameEt.setText(user.lastName)
            profileEmailEt.setText(user.email)
        }
    }

    private fun hideUserLoading() {
        binding.apply {
            profileSpinKit.visibility = View.GONE
            profileBtnSave.visibility = View.VISIBLE
            profileEmailEt.visibility = View.VISIBLE
            profileFirstNameEt.visibility = View.VISIBLE
            profileLastNameEt.visibility = View.VISIBLE
            profileForgetPasswordTxt.visibility = View.VISIBLE
            profileImageUser.visibility = View.VISIBLE
            profileImageEdit.visibility = View.VISIBLE
            profileToolbar.visibility = View.VISIBLE
        }
    }

    private fun showUserLoading() {
        binding.apply {
            profileSpinKit.visibility = View.VISIBLE
            profileBtnSave.visibility = View.INVISIBLE
            profileEmailEt.visibility = View.INVISIBLE
            profileFirstNameEt.visibility = View.INVISIBLE
            profileLastNameEt.visibility = View.INVISIBLE
            profileForgetPasswordTxt.visibility = View.INVISIBLE
            profileImageUser.visibility = View.INVISIBLE
            profileImageEdit.visibility = View.INVISIBLE
            profileToolbar.visibility = View.INVISIBLE
        }
    }
}