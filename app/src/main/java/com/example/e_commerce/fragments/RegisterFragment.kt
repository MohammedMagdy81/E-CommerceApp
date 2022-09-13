package com.example.e_commerce.fragments

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
import com.example.e_commerce.data.User
import com.example.e_commerce.databinding.FragmentRegisterBinding
import com.example.e_commerce.mvvm.RegisterViewModel
import com.example.e_commerce.utils.RegisterValidation
import com.example.e_commerce.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            registerBtnRegister.setOnClickListener {

                val user = User(
                    registerEditTextFirstName.text.toString().trim(),
                    registerEditTextLastName.text.toString().trim(),
                    registerEditTextEmail.text.toString().trim()
                )
                val password = registerEditTextPassword.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)

            }

            registerTextLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }



        observeToData()
        observeToRegisterValidation()

    }

    private fun observeToRegisterValidation() {
        lifecycleScope.launch {
            viewModel.registerChannel.collect {
                if (it.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.registerEditTextEmail.apply {
                            requestFocus()
                            error=it.email.message
                        }
                    }
                }

                if (it.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.registerEditTextPassword.apply {
                            requestFocus()
                            error=it.password.message
                        }
                    }
                }


            }
        }
    }

    private fun observeToData() {
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {
                when (it) {
                    is Resources.Loading -> {
                        loadingState()
                    }
                    is Resources.Success -> {
                       successState()
                    }
                    is Resources.Error -> {
                       errorState(it)
                    }
                }
            }
        }
    }

    private fun errorState(it: Resources.Error<User>) {
        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        binding.registerBtnRegister.revertAnimation()
    }

    private fun successState() {
        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
        binding.registerBtnRegister.revertAnimation()
    }

    private fun loadingState() {
        binding.registerBtnRegister.startAnimation()
    }
}