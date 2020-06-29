package com.example.nasaphotooftheday.apimanager

interface ApiResponseCallback {
    fun onSuccess(obj: Any)
    fun onFailure(message: String)
    fun onError(message: String)
}