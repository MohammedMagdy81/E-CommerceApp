package com.example.e_commerce.fragments.login_register

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
import com.example.e_commerce.dialog.setUpDialogSheet
import com.example.e_commerce.mvvm.LoginViewModel
import com.example.e_commerce.utils.Resources
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            loginBtnLogin.setOnClickListener {
                val email = loginEditTextEmail.text.toString().trim()
                val password = loginEditTextPassword.text.toString()
                viewModel.loginUser(email, password)
            }
            loginTextRegisterHere.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            loginTextForgetPassword.setOnClickListener {
                setUpDialogSheet { email ->
                    viewModel.resetPassword(email)
                }
            }
        }

        observeToSharedFlow()
        observeToResetPasswordState()
    }

    private fun observeToResetPasswordState() {
        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect {
                when(it){
                    is Resources.Loading->{

                    }
                    is Resources.Success->{
                        Toasty.success(
                            requireContext(),
                            " Login Successfully ..",
                            Toast.LENGTH_LONG,
                            true
                        ).show()
                    }
                    is Resources.Error->{
                        Toasty.error(
                            requireContext(),
                            it.message.toString(),
                            Toast.LENGTH_LONG,
                            true
                        ).show()
                    }
                }
            }
        }
    }

    private fun observeToSharedFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Resources.Loading -> {
                        binding.loginBtnLogin.startAnimation()
                    }
                    is Resources.Success -> {
                        binding.loginBtnLogin.revertAnimation()
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    is Resources.Error -> {
                        binding.loginBtnLogin.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

    }


}