package com.ankitdubey021.gigrangmvvm.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ankitdubey021.gigrangmvvm.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}