package com.example.e_commerce.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.activities.ShoppingActivity
import com.example.e_commerce.databinding.FragmentLoginBinding
import com.example.e_commerce.dialog.setupResetPasswordDialog
import com.example.e_commerce.mvvm.LoginViewModel
import com.example.e_commerce.utils.RegisterValidation
import com.example.e_commerce.utils.Resources
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            loginTvRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            loginTvForgetPassword.setOnClickListener {
                showBottomSheetForgetPassword()
            }

            loginBtnLogin.setOnClickListener {
                val email = loginEdTEmail.text.toString().trim()
                val password = loginEdTPassword.text.toString()
                viewModel.loginUser(email, password)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect {
                when (it) {
                    is Resources.Loading -> {
                        binding.loginBtnLogin.startAnimation()
                    }
                    is Resources.Error -> {
                        binding.loginBtnLogin.revertAnimation()
                        Toasty.error(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resources.Success -> {
                        binding.loginBtnLogin.revertAnimation()
                        goToShoppingActivity()
                    }
                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.loginField.collect {
                if (it.email is RegisterValidation.Field) {
                    withContext(Dispatchers.Main) {
                        binding.loginEdTEmail.requestFocus()
                        binding.loginEdTEmail.error = it.email.message
                    }
                }
                if (it.password is RegisterValidation.Field) {
                    withContext(Dispatchers.Main) {
                        binding.loginEdTPassword.requestFocus()
                        binding.loginEdTPassword.error = it.password.message
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect {
                when (it) {
                    is Resources.Error -> {
                        binding.spinKit.visibility = View.GONE
                        Toasty.error(requireContext(), "some error happen ! try again")
                    }
                    is Resources.Loading -> {
                        binding.spinKit.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.spinKit.visibility = View.GONE
                        Snackbar.make(
                            requireView(),
                            "link for reset password sent successfully ",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showBottomSheetForgetPassword() {
        setupResetPasswordDialog {
            viewModel.resetPassword(email = it)
        }
    }

    private fun goToShoppingActivity() {
        val intent = Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }


}