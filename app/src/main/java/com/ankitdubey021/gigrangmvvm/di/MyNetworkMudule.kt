package com.ankitdubey021.gigrangmvvm.di

import com.ankitdubey021.gigrangmvvm.data.AUTHORIZATION
import com.ankitdubey021.gigrangmvvm.networking.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
class NetworkModule{
    var defaultTimeout = 180

    @Provides
    @Singleton
    fun provideCall(sdf: SharedPrefsHelper): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(defaultTimeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(defaultTimeout.toLong(), TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->

                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", sdf[AUTHORIZATION, ""]!!)
                    .build()

                chain.proceed(newRequest)
            })
            .build()
        return Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesNetworkService(
        retrofit: Retrofit
    ): ApiService = retrofit.create(ApiService::class.java)

}