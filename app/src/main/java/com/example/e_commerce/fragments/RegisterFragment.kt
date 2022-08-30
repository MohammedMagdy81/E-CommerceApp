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
import com.example.e_commerce.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment:Fragment() {
    private lateinit var binding:FragmentRegisterBinding
    private val registerViewModel:RegisterViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
    }

    private fun initClickListener() {
        binding.apply {

            registerButtonRegister.setOnClickListener {
                val user=User(registerEditTextFirstName.text.toString(),
                    registerEditTextLastName.text.toString(),
                    registerEditTextEMail.text.toString(),
                )
                val password= registerEditTextPassword.text.toString()
                registerViewModel.createUserWithEmailAndPassword(user,password)
            }

        }

        lifecycleScope.launch{
             registerViewModel.register.collect{
                 when(it){
                     is Resources.Loading->{
                         startAnimation()
                     }
                     is Resources.Success->{
                         stopAnimation()
                         Toast.makeText(context, "User Created Successfully", Toast.LENGTH_LONG).show()
                     }
                     is Resources.Error->{
                         stopAnimation()
                         Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
                     }
                     is Resources.UnSpecify->{
                         stopAnimation()
                     }

                 }
             }
        }

    }









    private fun stopAnimation() {
        binding.registerButtonRegister.revertAnimation()
    }

    private fun startAnimation() {
        binding.registerButtonRegister.startAnimation()
    }


}