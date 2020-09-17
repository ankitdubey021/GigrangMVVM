package com.ankitdubey021.gigrangmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ankitdubey021.gigrangmvvm.R
import com.ankitdubey021.gigrangmvvm.commons.utils.launchActivity
import com.ankitdubey021.gigrangmvvm.data.AUTHORIZATION
import com.ankitdubey021.gigrangmvvm.di.SharedPrefsHelper
import com.ankitdubey021.gigrangmvvm.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class Splash : AppCompatActivity() {

    private lateinit var job : Job
    @Inject lateinit var sharedPrefsHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        job  =  GlobalScope.launch {
            delay(1500)
            proceed()
        }
    }

    private fun proceed(){
        if(sharedPrefsHelper[AUTHORIZATION, ""]!!.isNotEmpty())
            launchActivity<Home> {  }
        else
            launchActivity<AuthActivity> { }

        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        job.cancel()
    }
}