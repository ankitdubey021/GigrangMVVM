package com.ankitdubey021.gigrangmvvm.ui.auth.login

import android.content.Context
import android.provider.Settings
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitdubey021.gigrangmvvm.R
import com.ankitdubey021.gigrangmvvm.commons.isValidEmail
import com.ankitdubey021.gigrangmvvm.commons.toast
import com.ankitdubey021.gigrangmvvm.commons.utils.State
import com.ankitdubey021.gigrangmvvm.commons.utils.giveMeJson
import com.ankitdubey021.gigrangmvvm.commons.utils.with
import com.ankitdubey021.gigrangmvvm.data.repository.LoginRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.ResponseBody


class LoginViewModel @ViewModelInject constructor(
    @ApplicationContext  private val context : Context,
    private val repository: LoginRepository
): ViewModel(){

    private val deviceId by lazy {
        Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }
    private val _postsLiveData = MutableLiveData<State<ResponseBody>>()

    val loginLiveData: LiveData<State<ResponseBody>>
        get() = _postsLiveData

    var mail = ""
    var password = ""

    fun validate(){
        if(mail.isEmpty())
            context.toast(context.getString(R.string.err_mail_mandatory))

        else if(!mail.isValidEmail())
            context.toast(context.getString(R.string.err_mail_invalid))

        else if(password.isEmpty())
            context.toast(context.getString(R.string.err_password_mandatory))

        else if(password.length<4)
            context.toast(context.getString(R.string.err_password_length))

        else
            login()

    }

    private fun login() {

            val json = giveMeJson {
                with("email",mail)
                with("password",password)
                with("deviceType","android")
                with("deviceId",deviceId)
            }

            viewModelScope.launch {
                repository.getData(json).collect{
                    _postsLiveData.value = it
                }
            }
    }
}