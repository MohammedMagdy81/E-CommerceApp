package com.example.e_commerce.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.R
import com.example.e_commerce.data.User
import com.example.e_commerce.databinding.FragmentRegisterBinding
import com.example.e_commerce.mvvm.RegisterViewModel
import com.example.e_commerce.utils.RegisterFailedState
import com.example.e_commerce.utils.RegisterValidation
import com.example.e_commerce.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
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
                        loading()
                    }
                    is Resources.Success -> {
                        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                        binding.registerBtnRegister.revertAnimation()
                    }
                    is Resources.Error -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        binding.registerBtnRegister.revertAnimation()
                    }
                }
            }
        }
    }

    private fun loading() {
        binding.registerBtnRegister.startAnimation()
    }
}