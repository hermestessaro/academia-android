package com.example.academia.WebService

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiCalls {

    fun <T:Any> getListApi(call: Call<ApiResponse<T>>): List<T>? {
        var list:List<T>? = null

        call.enqueue(object : Callback<ApiResponse<T>> {
            override fun onResponse(call: Call<ApiResponse<T>>, response: Response<ApiResponse<T>>) {
                list = response.body()!!.items
            }

            override fun onFailure(call: Call<ApiResponse<T>>, t: Throwable) {
                Log.e("onFailure error", t.message!!)
            }
        })

        return list
    }


}