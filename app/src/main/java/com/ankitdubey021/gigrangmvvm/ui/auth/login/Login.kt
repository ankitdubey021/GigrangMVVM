package com.ankitdubey021.gigrangmvvm.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ankitdubey021.gigrangmvvm.commons.toast
import com.ankitdubey021.gigrangmvvm.commons.utils.ProgressBarUtils
import com.ankitdubey021.gigrangmvvm.commons.utils.State
import com.ankitdubey021.gigrangmvvm.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class Login : Fragment() {

    lateinit var binding : FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater,container,false).apply {
            viewmodel = loginViewModel
        }

        initLoginObserver()
        return binding.root
    }

    private fun initLoginObserver() {

        loginViewModel.loginLiveData.observe(viewLifecycleOwner, Observer {state->
            when(state){
                is State.Loading-> ProgressBarUtils.showProgressDialog(requireContext())

                is State.Success-> {
                    ProgressBarUtils.removeProgressDialog()
                    requireContext().toast(state.data.string())
                    Timber.i(state.data.string())
                }
                is State.Error -> {
                    ProgressBarUtils.removeProgressDialog()
                    requireContext().toast(state.message.string())
                    Timber.e(state.message.string())
                }
            }
        })
    }
}