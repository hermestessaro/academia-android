package com.example.academia.controllers.WebService

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {

    val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.5/academia/web/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    fun appServices() = retrofit.create(AppServices::class.java)

}