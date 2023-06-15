package com.example.e_commerce.fragments.shopping

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.e_commerce.BuildConfig
import com.example.e_commerce.R
import com.example.e_commerce.activities.LoginRegisterActivity
import com.example.e_commerce.data.User
import com.example.e_commerce.databinding.FragmentProfileBinding
import com.example.e_commerce.mvvm.ProfileViewModel
import com.example.e_commerce.utils.Resources
import com.example.e_commerce.utils.hideBottomNav
import com.example.e_commerce.utils.showBottomNav
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            constraintProfile.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
                hideBottomNav()
            }
            linearOrders.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
                hideBottomNav()
            }

            linearLanguage.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_languageFragment)
                hideBottomNav()
            }
            linearBilling.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToBillingFragment(
                    0f,
                    emptyArray()
                )
                findNavController().navigate(action)
                hideBottomNav()
            }
            linearOut.setOnClickListener {
                binding.profileSpinKit.visibility = View.VISIBLE

                viewModel.logout()
                val intent = Intent(requireActivity(), LoginRegisterActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            }
            tvVersionCode.text = "Version ${BuildConfig.VERSION_CODE}"
            collectState()
        }

    }

    private fun collectState() {
        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when (it) {
                    is Resources.Error -> {
                        binding.profileSpinKit.visibility = View.GONE
                        Toasty.error(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resources.Loading -> {
                        binding.profileSpinKit.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.profileSpinKit.visibility = View.GONE
                        it.data?.let { user ->
                            if (user.imagePath.isNotEmpty())
                                Glide.with(requireView()).load(it.data.imagePath)
                                    .into(binding.imgUser)
                            binding.tvUserName.text = "${it.data.firstName} ${it.data.lastName}"
                        }

                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupUserData(user: User) {
        Glide.with(this@ProfileFragment).load(user.imagePath).error(R.drawable.ic_person)
            .into(binding.imgUser)
        binding.tvUserName.text = "${user.firstName} ${user.lastName}"

    }

    override fun onResume() {
        super.onResume()
        showBottomNav()
    }

}












