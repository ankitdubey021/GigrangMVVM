package com.ankitdubey021.gigrangmvvm.networking
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {

    @POST
    suspend fun doPostApiCall(
        @Url url : String,
        @Body jsonObject: JsonObject): Response<ResponseBody>

    companion object {
        const val BASE_URL = "http://13.232.11.227/api/"
    }
}