package com.example.academia.controllers.WebService

import com.example.academia.models.ProfessorModel
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET

interface AppServices {

    @GET("/academia/web/api")
    fun getProfessors(): Call<List<ProfessorModel>>
}