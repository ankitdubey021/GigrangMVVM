package com.ankitdubey021.gigrangmvvm.data.repository

import com.ankitdubey021.gigrangmvvm.commons.utils.State
import com.ankitdubey021.gigrangmvvm.networking.ApiService
import com.ankitdubey021.gigrangmvvm.networking.apiLogin
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LoginRepository @Inject constructor(
    private val apiService: ApiService
){

    fun getData(json : JsonObject) =
        flow<State<ResponseBody>>{

            emit(State.loading())

            val response = apiService.doPostApiCall(apiLogin,json)

            if(response.isSuccessful)
                emit(State.success(response.body()!!))
            else
                emit(State.error(response.errorBody()!!))
        }
}