package com.example.nasaphotooftheday.apimanager

import android.content.Context
import com.example.nasaphotooftheday.R
import com.example.nasaphotooftheday.models.PhotoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class RestApiManager  private constructor(val mContext: Context)  {

    private val tag: String? = "RestApiManager"
    private val apiClient: APIClient = APIClient()


    companion object {
        private var instance: RestApiManager? = null
        fun getInstance(mContext: Context): RestApiManager {
            if (instance == null)
                instance = RestApiManager(mContext)
            return instance as RestApiManager
        }
    }

    fun callPhotoApi(apiKey: String, responseCallback: ApiResponseCallback) {
        val call = apiClient.adapter.getPhoto(apiKey)
        try {
            call?.enqueue(object : Callback<PhotoResponse?> {
                override fun onFailure(call: Call<PhotoResponse?>, t: Throwable) {
                    if (t is ConnectException) {
                        responseCallback.onFailure(mContext.getString(R.string.str_connectivity_lost))
                    } else {
                        responseCallback.onFailure(t.localizedMessage)
                    }
                }

                override fun onResponse(call: Call<PhotoResponse?>, response: Response<PhotoResponse?>) {
                    if (response.body() != null) {
                        responseCallback.onSuccess(response.body()!!)
                    } else {
                        responseCallback.onError(response.message().toString())
                    }
                }
            })

        } catch (ex: Exception) {
            ex.printStackTrace()
            responseCallback.onFailure(ex.localizedMessage)
        }
    }

    fun callDatedPhotoApi(apiKey: String, date:String, responseCallback: ApiResponseCallback) {
        val call = apiClient.adapter.getPhotoByDate(apiKey,date)
        try {
            call?.enqueue(object : Callback<PhotoResponse?> {
                override fun onFailure(call: Call<PhotoResponse?>, t: Throwable) {
                    if (t is ConnectException) {
                        responseCallback.onFailure(mContext.getString(R.string.str_connectivity_lost))
                    } else {
                        responseCallback.onFailure(t.localizedMessage)
                    }
                }

                override fun onResponse(call: Call<PhotoResponse?>, response: Response<PhotoResponse?>) {
                    if (response.body() != null) {
                        responseCallback.onSuccess(response.body()!!)
                    } else {
                        responseCallback.onError(response.message().toString())
                    }
                }
            })

        } catch (ex: Exception) {
            ex.printStackTrace()
            responseCallback.onFailure(ex.localizedMessage)
        }
    }
}