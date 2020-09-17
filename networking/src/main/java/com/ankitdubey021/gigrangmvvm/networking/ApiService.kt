package com.ankitdubey021.gigrangmvvm.networking
import com.ankitdubey021.gigrangmvvm.extensions.Json
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {

    @POST
    suspend fun doPostApiCall(
        @Url url : String,
        @Body jsonObject: Json
    ): Response<ResponseBody>

    @GET
    suspend fun doGetApiCall(
        @Url url : String
    ): Response<ResponseBody>

    companion object {
        const val BASE_URL = "http://13.232.11.227/api/"
    }
}