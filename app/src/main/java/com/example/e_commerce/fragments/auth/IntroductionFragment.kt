package com.example.e_commerce.fragments.auth

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
import com.example.e_commerce.utils.Constants
import com.example.e_commerce.utils.Constants.NAVIGATE_TO_ACCOUNT_OPTION
import com.example.e_commerce.utils.Constants.NAVIGATE_TO_SHOPPING
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
        binding = FragmentIntroductionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.introductionBtnStart.setOnClickListener {
            viewModel.makeButtonClick()
            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionFragment)
        }
        lifecycleScope.launchWhenStarted {
            viewModel.navigateState.collect {
                when (it) {
                    NAVIGATE_TO_SHOPPING -> {
//                        val intent =
//                            Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                            }
//                        startActivity(intent)
                    }
                    NAVIGATE_TO_ACCOUNT_OPTION -> {
                        //findNavController().navigate(R.id.action_introductionFragment_to_accountOptionFragment)
                    }
                }
            }
        }
    }

}