package com.ankitdubey021.gigrangmvvm.data.repository

import com.ankitdubey021.gigrangmvvm.commons.utils.State
import com.ankitdubey021.gigrangmvvm.commons.utils.giveMeJson
import com.ankitdubey021.gigrangmvvm.commons.utils.with
import com.ankitdubey021.gigrangmvvm.networking.ApiService
import com.ankitdubey021.gigrangmvvm.networking.apiCategories
import com.ankitdubey021.gigrangmvvm.networking.apiFetchDevelopers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val apiService: ApiService
){

    fun getDevelopers(categoryId : String?, page : Int, length : Int) : Flow<State<ResponseBody>>{

        val json = giveMeJson {
            with("page",page)
            with("category",categoryId)
            with("length",length)
        }

        return flow {
            emit(State.loading())
            val response = apiService.doPostApiCall(apiFetchDevelopers,json)
            if (response.isSuccessful)
                emit(State.success(response.body()!!))
            else
                emit(State.error(response.errorBody()!!))
        }

    }

    fun fetchCategories() =
        flow<State<ResponseBody>>{
            emit(State.loading())

            val response = apiService.doGetApiCall(apiCategories)

            if(response.isSuccessful)
                emit(State.success(response.body()!!))
            else
                emit(State.error(response.errorBody()!!))
        }

}