package com.example.e_commerce.fragments.login_register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentAccountOptionsBinding


class AccountOptionFragment : Fragment() {
    private lateinit var binding:FragmentAccountOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAccountOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            accountOptionBtnLogin.setOnClickListener{
                findNavController().navigate(R.id.action_accountOptionFragment_to_loginFragment)
            }
            accountOptionBtnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_accountOptionFragment_to_registerFragment)
            }
        }
    }

}