package com.ankitdubey021.gigrangmvvm.screens.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import com.ankitdubey021.gigrangmvvm.utils.ProgressBarUtils
import com.ankitdubey021.gigrangmvvm.networking.State

import com.ankitdubey021.gigrangmvvm.data.AUTHORIZATION
import com.ankitdubey021.gigrangmvvm.databinding.FragmentLoginBinding
import com.ankitdubey021.gigrangmvvm.di.SharedPrefsHelper
import com.ankitdubey021.gigrangmvvm.extensions.launchActivity
import com.ankitdubey021.gigrangmvvm.extensions.toast
import com.ankitdubey021.gigrangmvvm.screens.Home
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class Login : Fragment() {

    @Inject lateinit var prefsHelper: SharedPrefsHelper
    private val loginViewModel: LoginViewModel by viewModels()

    lateinit var binding : FragmentLoginBinding

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
                    proceedToHome(state.data)
                }

                is State.Error -> {
                    ProgressBarUtils.removeProgressDialog()
                    requireContext().toast(state.message.string())
                    Timber.e(state.message.string())
                }
            }
        })
    }

    private fun proceedToHome(data: ResponseBody) {
        val res = JSONObject(data.string())
        prefsHelper.put(AUTHORIZATION, "Bearer " + res.getString("token"))

        activity?.let {
            it.launchActivity<Home> {  }
            it.finish()
        }
    }
}