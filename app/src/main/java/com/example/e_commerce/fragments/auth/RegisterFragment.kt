package com.example.e_commerce.fragments.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.activities.ShoppingActivity
import com.example.e_commerce.databinding.FragmentRegisterBinding
import com.example.e_commerce.mvvm.RegisterViewModel
import com.example.e_commerce.data.User
import com.example.e_commerce.utils.RegisterValidation
import com.example.e_commerce.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            registerTVLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
            registerBtnRegister.setOnClickListener {
                createNewUser()
            }
            observeToRegisterState()
        }
    }

    private fun observeToRegisterState() {
        lifecycleScope.launchWhenStarted {
            registerViewModel.registerState.collect {
                when (it) {
                    is Resources.Success -> {
                        binding.registerBtnRegister.revertAnimation()
                        Toasty.success(requireContext(), "تم التسجيل بنجاح ", Toast.LENGTH_LONG)
                            .show()
                        goToShoppingActivity()
                    }
                    is Resources.Error -> {
                        binding.registerBtnRegister.revertAnimation()
                        Toasty.error(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resources.Ideal -> {}
                    is Resources.Loading -> {
                        binding.registerBtnRegister.startAnimation()
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            registerViewModel.validation.collect {
                if (it.email is RegisterValidation.Field) {
                    withContext(Dispatchers.Main) {
                        binding.registerEDEmail.requestFocus()
                        binding.registerEDEmail.error = it.email.message
                    }
                }
                if (it.password is RegisterValidation.Field) {
                    withContext(Dispatchers.Main) {
                        binding.registerEDPassword.requestFocus()
                        binding.registerEDPassword.error = it.password.message
                    }
                }
            }
        }
    }

    private fun goToShoppingActivity() {
        val intent = Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    private fun createNewUser() {
        binding.apply {
            val user = User(
                firstName = registerEDFirstName.text.toString().trim(),
                lastName = registerEDLastName.text.toString().trim(),
                email = registerEDEmail.text.toString().trim()
            )
            registerViewModel.registerUser(user, registerEDPassword.text.toString())
        }
    }

}