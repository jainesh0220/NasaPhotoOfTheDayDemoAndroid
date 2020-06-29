package com.example.nasaphotooftheday.apimanager


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class APIClient {
    val adapter: ApiInterface by lazy {
        val httpClient = OkHttpClient.Builder()

        val client: OkHttpClient = httpClient.build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(WebWareHouse.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return@lazy retrofit.create(ApiInterface::class.java)
    }
}
