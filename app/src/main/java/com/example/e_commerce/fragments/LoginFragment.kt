package com.example.e_commerce.fragments

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
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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
                    Snackbar.make(requireView(),"Password Reset Link Send Successfully",Snackbar.LENGTH_LONG).show()
                    }
                    is Resources.Error->{
                        Snackbar.make(requireView(),"Error :${it.message}",Snackbar.LENGTH_LONG).show()
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