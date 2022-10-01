package com.example.e_commerce.fragments.login_register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.activities.ShoppingActivity
import com.example.e_commerce.databinding.FragmentIntroductionBinding
import com.example.e_commerce.mvvm.IntroductionViewModel
import com.example.e_commerce.mvvm.IntroductionViewModel.Companion.ACCOUNT_OPTION_FRAGMENT
import com.example.e_commerce.mvvm.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroductionFragment : Fragment() {
    private lateinit var binding: FragmentIntroductionBinding
    private val viewModel by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.navigateState.collect { state ->
                when (state) {
                    SHOPPING_ACTIVITY -> {

                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }

                    }
                    ACCOUNT_OPTION_FRAGMENT->{
                        findNavController().navigate(R.id.action_introductionFragment_to_accountOptionFragment)
                    }
                }
            }
        }

        binding.introductionBtnStart.setOnClickListener {
            viewModel.setUpButtonClick()
            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionFragment)
        }
    }

}