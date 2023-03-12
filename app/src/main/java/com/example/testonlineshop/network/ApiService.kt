package com.example.testonlineshop.network

import com.example.testonlineshop.model.FlashSale
import com.example.testonlineshop.model.Latest
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://run.mocky.io/v3/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface GetDataApiService {

    @GET("cc0071a1-f06e-48fa-9e90-b1c2a61eaca7")
    suspend fun getLatest(): Response<Latest>

    @GET("a9ceeb6e-416d-4352-bde6-2203416576ac")
    suspend fun getFlashSale(): Response<FlashSale>

}

object GetApiService {
    val retrofitService: GetDataApiService by lazy {
        retrofit.create(GetDataApiService::class.java)
    }
}