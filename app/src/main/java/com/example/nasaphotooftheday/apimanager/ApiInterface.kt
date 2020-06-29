package com.example.nasaphotooftheday.apimanager

import com.example.nasaphotooftheday.models.PhotoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET(WebWareHouse.PHOTO_URL)
    fun getPhoto(@Query(WebWareHouse.API_KEY_PARAM) api_key: String?): Call<PhotoResponse?>?

    @GET(WebWareHouse.PHOTO_URL)
    fun getPhotoByDate(@Query(WebWareHouse.API_KEY_PARAM) api_key: String?,@Query(WebWareHouse.API_KEY_DATE) date: String?): Call<PhotoResponse?>?
}